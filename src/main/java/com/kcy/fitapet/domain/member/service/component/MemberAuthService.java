package com.kcy.fitapet.domain.member.service.component;

import com.kcy.fitapet.domain.member.domain.Member;
import com.kcy.fitapet.domain.member.dto.auth.SignInReq;
import com.kcy.fitapet.domain.member.dto.auth.SignUpReq;
import com.kcy.fitapet.domain.member.dto.sms.SensRes;
import com.kcy.fitapet.domain.member.dto.sms.SmsReq;
import com.kcy.fitapet.domain.member.dto.sms.SmsRes;
import com.kcy.fitapet.domain.member.service.module.MemberSaveService;
import com.kcy.fitapet.domain.member.service.module.MemberSearchService;
import com.kcy.fitapet.domain.member.service.module.SmsService;
import com.kcy.fitapet.global.common.response.code.ErrorCode;
import com.kcy.fitapet.global.common.response.exception.GlobalErrorException;
import com.kcy.fitapet.global.common.util.jwt.JwtUtil;
import com.kcy.fitapet.global.common.util.jwt.entity.JwtUserInfo;
import com.kcy.fitapet.global.common.util.redis.forbidden.ForbiddenTokenService;
import com.kcy.fitapet.global.common.util.redis.refresh.RefreshToken;
import com.kcy.fitapet.global.common.util.redis.refresh.RefreshTokenService;
import com.kcy.fitapet.global.common.util.redis.sms.SmsCertificationService;
import com.nimbusds.oauth2.sdk.SuccessResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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
    private final SmsService smsService;

    private final RefreshTokenService refreshTokenService;
    private final ForbiddenTokenService forbiddenTokenService;
    private final SmsCertificationService smsCertificationService;
    private final JwtUtil jwtUtil;

    private final PasswordEncoder bCryptPasswordEncoder;

    @Transactional
    public Map<String, String> register(String authHeader, SignUpReq dto) {
        String parsedToken = jwtUtil.resolveToken(authHeader);
        String authenticatedPhone = jwtUtil.getPhoneNumberFromToken(parsedToken);

        if (!smsCertificationService.isCorrectCertificationNumber(authenticatedPhone, parsedToken))
            throw new GlobalErrorException(ErrorCode.INVALID_AUTH_CODE);
        smsCertificationService.removeCertificationNumber(authenticatedPhone);

        Member requestMember = dto.toEntity(authenticatedPhone);
        requestMember.encodePassword(bCryptPasswordEncoder);
        log.debug("회원가입 요청: {}", requestMember);

        if (memberSearchService.isExistMemberByUidOrEmailOrPhone(requestMember.getUid(), requestMember.getEmail(), requestMember.getPhone()))
            throw new GlobalErrorException(ErrorCode.DUPLICATE_USER_INFO_ERROR);

        Member registeredMember = memberSaveService.saveMember(requestMember);
        log.debug("회원가입 완료: {}", registeredMember);
        JwtUserInfo jwtUserInfo = JwtUserInfo.from(registeredMember);

        return generateToken(jwtUserInfo);
    }

    @Transactional
    public Map<String, String> login(SignInReq dto) {
        Member member = memberSearchService.getMemberByUid(dto.uid());
        if (member.checkPassword(dto.password(), bCryptPasswordEncoder))
            throw new GlobalErrorException(ErrorCode.NOT_MATCH_PASSWORD_ERROR);

        JwtUserInfo jwtUserInfo = JwtUserInfo.from(member);

        return generateToken(jwtUserInfo);
    }

    @Transactional
    public void logout(String authHeader, String requestRefreshToken) {
        String accessToken = jwtUtil.resolveToken(authHeader);
        Long userId = jwtUtil.getUserIdFromToken(accessToken);

        refreshTokenService.logout(requestRefreshToken);
        forbiddenTokenService.register(accessToken, userId);
    }

    @Transactional
    public Map<String, String> refresh(String requestRefreshToken) {
        RefreshToken refreshToken = refreshTokenService.refresh(requestRefreshToken);

        Long memberId = refreshToken.getUserId();
        JwtUserInfo dto = JwtUserInfo.from(memberSearchService.getMemberById(memberId));
        String accessToken = jwtUtil.generateAccessToken(dto);

        return Map.of(ACCESS_TOKEN.getValue(), accessToken, REFRESH_TOKEN.getValue(), refreshToken.getToken());
    }

    @Transactional
    public SmsRes sendCertificationNumber(SmsReq dto) {
        if (memberSearchService.isExistMemberByPhone(dto.to()))
            throw new GlobalErrorException(ErrorCode.DUPLICATE_PHONE_ERROR);
        String certificationNumber = smsCertificationService.issueCertificationNumber(dto.to());

        SensRes sensRes;
        try {
            sensRes = smsService.sendCertificationNumber(dto, certificationNumber);
        } catch (Exception e) {
            log.error("SMS 발송 실패: {}", e.getMessage());
            smsCertificationService.removeCertificationNumber(dto.to());
            throw new GlobalErrorException(ErrorCode.SMS_SEND_ERROR);
        }

        checkSmsStatus(dto.to(), sensRes);

        LocalDateTime expireTime = smsCertificationService.getExpiredTime(dto.to());
        log.info("인증번호 만료 시간: {}", expireTime);
        return SmsRes.of(dto.to(), sensRes.requestTime(), expireTime);
    }

    @Transactional
    public String checkCertificationNumber(SmsReq smsReq, String requestCertificationNumber) {
        String token = null;
        if (smsCertificationService.isCorrectCertificationNumber(smsReq.to(), requestCertificationNumber)) {
            log.info("인증번호 일치");
            token = smsCertificationService.issueSmsAuthToken(smsReq.to());
        }

        return (token != null) ? token : "";
    }

    private void checkSmsStatus(String requestPhone, SensRes sensRes) {
        if (sensRes.statusCode().equals("404")) {
            log.error("존재하지 않는 수신자: {}", requestPhone);
            smsCertificationService.removeCertificationNumber(requestPhone);
            throw new GlobalErrorException(ErrorCode.INVALID_RECEIVER);
        } else if (sensRes.statusName().equals("fail")) {
            log.error("SMS API 응답 실패: {}", sensRes);
            smsCertificationService.removeCertificationNumber(requestPhone);
            throw new GlobalErrorException(ErrorCode.SMS_SEND_ERROR);
        }
        log.info("SMS 발송 성공");
    }

    private void validateMember(Member member) {
        if (memberSearchService.isExistMemberByUidOrEmailOrPhone(member.getUid(), member.getEmail(), member.getPhone()))
            throw new GlobalErrorException(ErrorCode.DUPLICATE_USER_INFO_ERROR);
    }

    private Map<String, String> generateToken(JwtUserInfo jwtUserInfo) {
        String accessToken = jwtUtil.generateAccessToken(jwtUserInfo);
        String refreshToken = refreshTokenService.issueRefreshToken(accessToken);
        log.debug("accessToken : {}, refreshToken : {}", accessToken, refreshToken);

        return Map.of(ACCESS_TOKEN.getValue(), accessToken, REFRESH_TOKEN.getValue(), refreshToken);
    }
}
