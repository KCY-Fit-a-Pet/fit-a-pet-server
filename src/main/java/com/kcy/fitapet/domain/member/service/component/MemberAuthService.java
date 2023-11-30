package com.kcy.fitapet.domain.member.service.component;

import com.kcy.fitapet.domain.member.domain.Member;
import com.kcy.fitapet.domain.member.dto.auth.SignInReq;
import com.kcy.fitapet.domain.member.dto.auth.SignUpReq;
import com.kcy.fitapet.domain.member.exception.AccountErrorCode;
import com.kcy.fitapet.domain.member.exception.SmsErrorCode;
import com.kcy.fitapet.domain.member.service.module.MemberSaveService;
import com.kcy.fitapet.domain.member.service.module.MemberSearchService;
import com.kcy.fitapet.global.common.resolver.access.AccessToken;
import com.kcy.fitapet.global.common.response.code.ErrorCode;
import com.kcy.fitapet.global.common.response.exception.GlobalErrorException;
import com.kcy.fitapet.global.common.util.jwt.JwtUtil;
import com.kcy.fitapet.global.common.util.jwt.entity.JwtUserInfo;
import com.kcy.fitapet.global.common.util.jwt.entity.SmsAuthInfo;
import com.kcy.fitapet.global.common.util.redis.forbidden.ForbiddenTokenService;
import com.kcy.fitapet.global.common.util.redis.refresh.RefreshToken;
import com.kcy.fitapet.global.common.util.redis.refresh.RefreshTokenService;
import com.kcy.fitapet.global.common.util.redis.sms.SmsCertificationService;
import com.kcy.fitapet.global.common.util.redis.sms.SmsPrefix;
import com.kcy.fitapet.global.common.util.sms.SmsProvider;
import com.kcy.fitapet.global.common.util.sms.dto.SensRes;
import com.kcy.fitapet.global.common.util.sms.dto.SmsReq;
import com.kcy.fitapet.global.common.util.sms.dto.SmsRes;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.Map;

import static com.kcy.fitapet.global.common.util.jwt.AuthConstants.ACCESS_TOKEN;
import static com.kcy.fitapet.global.common.util.jwt.AuthConstants.REFRESH_TOKEN;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberAuthService {
    private final MemberSearchService memberSearchService;
    private final MemberSaveService memberSaveService;

    private final RefreshTokenService refreshTokenService;
    private final ForbiddenTokenService forbiddenTokenService;
    private final SmsCertificationService smsCertificationService;

    private final SmsProvider smsProvider;
    private final JwtUtil jwtUtil;

    private final PasswordEncoder bCryptPasswordEncoder;

    @Transactional
    public Map<String, String> register(String requestAccessToken, SignUpReq dto) {
        String accessToken = jwtUtil.resolveToken(requestAccessToken);

        String authenticatedPhone = jwtUtil.getPhoneNumberFromToken(accessToken);
        smsCertificationService.removeCertificationNumber(authenticatedPhone, SmsPrefix.REGISTER);

        Member requestMember = dto.toEntity(authenticatedPhone);
        requestMember.encodePassword(bCryptPasswordEncoder);
        validateMember(requestMember);

        Member registeredMember = memberSaveService.saveMember(requestMember);

        return generateToken(JwtUserInfo.from(registeredMember));
    }

    @Transactional
    public Map<String, String> login(SignInReq dto) {
        Member member = memberSearchService.findByUid(dto.uid());
        if (!member.checkPassword(dto.password(), bCryptPasswordEncoder))
            throw new GlobalErrorException(AccountErrorCode.NOT_MATCH_PASSWORD_ERROR);

        return generateToken(JwtUserInfo.from(member));
    }

    @Transactional
    public void logout(AccessToken requestAccessToken, String requestRefreshToken) {
        forbiddenTokenService.register(requestAccessToken);

        if (!StringUtils.hasText(requestRefreshToken))
            refreshTokenService.logout(requestRefreshToken);
    }

    @Transactional
    public Map<String, String> refresh(String requestRefreshToken) {
        RefreshToken refreshToken = refreshTokenService.refresh(requestRefreshToken);

        Long memberId = refreshToken.getUserId();
        JwtUserInfo dto = JwtUserInfo.from(memberSearchService.findById(memberId));
        String accessToken = jwtUtil.generateAccessToken(dto);

        return Map.of(ACCESS_TOKEN.getValue(), accessToken, REFRESH_TOKEN.getValue(), refreshToken.getToken());
    }

    @Transactional
    public SmsRes sendCertificationNumber(SmsReq dto, SmsPrefix prefix) {
        if (prefix.equals(SmsPrefix.REGISTER) && memberSearchService.isExistByPhone(dto.to()))
            throw new GlobalErrorException(AccountErrorCode.DUPLICATE_PHONE_ERROR);
        String certificationNumber = smsProvider.issueCertificationNumber(dto.to());

        SensRes sensRes;
        try {
            sensRes = smsProvider.sendCertificationNumber(dto, certificationNumber);
        } catch (Exception e) {
            log.warn("SMS 발송 실패: {}", e.getMessage());
            throw new GlobalErrorException(ErrorCode.SMS_SEND_ERROR);
        }
        checkSmsStatus(dto.to(), sensRes);

        smsCertificationService.saveSmsAuthToken(dto.to(), certificationNumber, SmsPrefix.REGISTER);
        LocalDateTime expireTime = smsCertificationService.getExpiredTime(dto.to(), SmsPrefix.REGISTER);
        log.info("인증번호 만료 시간: {}", expireTime);
        return SmsRes.of(dto.to(), sensRes.requestTime(), expireTime);
    }

    @Transactional
    public String checkCertificationForRegister(SmsReq smsReq, String requestCertificationNumber) {
        if (!smsCertificationService.isCorrectCertificationNumber(smsReq.to(), requestCertificationNumber, SmsPrefix.REGISTER)) {
            log.warn("인증번호 불일치 -> 사용자 입력 인증 번호 : {}", requestCertificationNumber);
            throw new GlobalErrorException(SmsErrorCode.INVALID_AUTH_CODE);
        }

        String token = jwtUtil.generateSmsAuthToken(SmsAuthInfo.of(1L, smsReq.to()));
        smsCertificationService.saveSmsAuthToken(smsReq.to(), token, SmsPrefix.REGISTER);

        return token;
    }

    @Transactional
    public void checkCertificationForSearch(SmsReq req, String requestCertificationNumber, SmsPrefix prefix) {
        if (!smsCertificationService.isCorrectCertificationNumber(req.to(), requestCertificationNumber, prefix)) {
            log.warn("인증번호 불일치 -> 사용자 입력 인증 번호 : {}", requestCertificationNumber);
            throw new GlobalErrorException(SmsErrorCode.INVALID_AUTH_CODE);
        }
    }

    private void checkSmsStatus(String requestPhone, SensRes sensRes) {
        if (sensRes.statusCode().equals("404")) {
            log.warn("존재하지 않는 수신자: {}", requestPhone);
            throw new GlobalErrorException(SmsErrorCode.INVALID_RECEIVER);
        } else if (sensRes.statusName().equals("fail")) {
            log.warn("SMS API 응답 실패: {}", sensRes);
            throw new GlobalErrorException(ErrorCode.SMS_SEND_ERROR);
        }
        log.info("SMS 발송 성공");
    }

    private void validateMember(Member member) {
        if (memberSearchService.isExistByUidOrEmailOrPhone(member.getUid(), member.getEmail(), member.getPhone()))
            throw new GlobalErrorException(AccountErrorCode.DUPLICATE_USER_INFO_ERROR);
    }

    private Map<String, String> generateToken(JwtUserInfo jwtUserInfo) {
        String accessToken = jwtUtil.generateAccessToken(jwtUserInfo);
        String refreshToken = refreshTokenService.issueRefreshToken(accessToken);
        log.debug("accessToken : {}, refreshToken : {}", accessToken, refreshToken);

        return Map.of(ACCESS_TOKEN.getValue(), accessToken, REFRESH_TOKEN.getValue(), refreshToken);
    }
}
