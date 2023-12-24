package com.kcy.fitapet.domain.member.service.component;

import com.kcy.fitapet.domain.member.domain.Member;
import com.kcy.fitapet.domain.member.dto.auth.SignInReq;
import com.kcy.fitapet.domain.member.dto.auth.SignUpReq;
import com.kcy.fitapet.domain.member.exception.AccountErrorCode;
import com.kcy.fitapet.domain.member.exception.SmsErrorCode;
import com.kcy.fitapet.domain.member.service.module.MemberSaveService;
import com.kcy.fitapet.domain.member.service.module.MemberSearchService;
import com.kcy.fitapet.global.common.redis.sms.SmsRedisHelper;
import com.kcy.fitapet.global.common.resolver.access.AccessToken;
import com.kcy.fitapet.global.common.response.code.StatusCode;
import com.kcy.fitapet.global.common.response.exception.GlobalErrorException;
import com.kcy.fitapet.global.common.security.jwt.JwtUtil;
import com.kcy.fitapet.global.common.security.jwt.dto.JwtUserInfo;
import com.kcy.fitapet.global.common.security.jwt.dto.SmsAuthInfo;
import com.kcy.fitapet.global.common.redis.forbidden.ForbiddenTokenService;
import com.kcy.fitapet.global.common.redis.refresh.RefreshToken;
import com.kcy.fitapet.global.common.redis.refresh.RefreshTokenService;
import com.kcy.fitapet.global.common.redis.sms.type.SmsPrefix;
import com.kcy.fitapet.global.common.security.jwt.exception.AuthErrorCode;
import com.kcy.fitapet.global.common.util.sms.SmsProvider;
import com.kcy.fitapet.global.common.util.sms.dto.SensInfo;
import com.kcy.fitapet.global.common.util.sms.dto.SmsReq;
import com.kcy.fitapet.global.common.util.sms.dto.SmsRes;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.Map;

import static com.kcy.fitapet.global.common.security.jwt.AuthConstants.ACCESS_TOKEN;
import static com.kcy.fitapet.global.common.security.jwt.AuthConstants.REFRESH_TOKEN;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberAuthService {
    private final MemberSearchService memberSearchService;
    private final MemberSaveService memberSaveService;

    private final RefreshTokenService refreshTokenService;
    private final ForbiddenTokenService forbiddenTokenService;

    private final SmsRedisHelper smsRedisHelper;

    private final SmsProvider smsProvider;
    private final JwtUtil jwtUtil;

    private final PasswordEncoder bCryptPasswordEncoder;

    @Transactional
    public Map<String, String> register(String requestAccessToken, SignUpReq dto) {
        String accessToken = jwtUtil.resolveToken(requestAccessToken);
        if (forbiddenTokenService.isForbidden(accessToken))
            throw new GlobalErrorException(AuthErrorCode.FORBIDDEN_ACCESS_TOKEN);

        String authenticatedPhone = jwtUtil.getPhoneNumberFromToken(accessToken);
        smsRedisHelper.removeCode(authenticatedPhone, SmsPrefix.REGISTER);

        Member requestMember = dto.toEntity(authenticatedPhone);
        requestMember.encodePassword(bCryptPasswordEncoder);
        validateMember(requestMember);

        Member registeredMember = memberSaveService.saveMember(requestMember);
        forbiddenTokenService.register(
                AccessToken.of(accessToken, jwtUtil.getUserIdFromToken(accessToken),
                        jwtUtil.getExpiryDate(accessToken), false)
        );

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
    public SmsRes sendCode(SmsReq dto, SmsPrefix prefix) {
        validateForSms(prefix, dto);
        SensInfo smsInfo = smsProvider.sendCodeByPhoneNumber(dto);

        smsRedisHelper.saveSmsAuthToken(dto.to(), smsInfo.code(), prefix);
        LocalDateTime expireTime = smsRedisHelper.getExpiredTime(dto.to(), prefix);
        log.info("인증번호 만료 시간: {}", expireTime);
        return SmsRes.of(dto.to(), smsInfo.requestTime(), expireTime);
    }

    @Transactional
    public String checkCodeForRegister(SmsReq smsReq, String requestCode) {
        if (!smsRedisHelper.isCorrectCode(smsReq.to(), requestCode, SmsPrefix.REGISTER)) {
            log.warn("인증번호 불일치 -> 사용자 입력 인증 번호 : {}", requestCode);
            throw new GlobalErrorException(SmsErrorCode.INVALID_AUTH_CODE);
        }
        smsRedisHelper.removeCode(smsReq.to(), SmsPrefix.REGISTER);
        return jwtUtil.generateSmsAuthToken(SmsAuthInfo.of(1L, smsReq.to()));
    }

    @Transactional(readOnly = true)
    public void checkCodeForSearch(SmsReq req, String code, SmsPrefix prefix) {
        if (!smsRedisHelper.isExistsCode(req.to(), prefix)) {
            StatusCode errorCode = SmsErrorCode.EXPIRED_AUTH_CODE;
            log.warn("인증번호 유효성 검사 실패: {}", errorCode);
            throw new GlobalErrorException(errorCode);
        }

        if (!smsRedisHelper.isCorrectCode(req.to(), code, prefix)) {
            StatusCode errorCode = SmsErrorCode.INVALID_AUTH_CODE;
            log.warn("인증번호 유효성 검사 실패: {}", errorCode);
            throw new GlobalErrorException(errorCode);
        }
    }

    private void validateForSms(SmsPrefix prefix, SmsReq req) {
        boolean isExistPhone = memberSearchService.isExistByPhone(req.to());
        if (prefix.equals(SmsPrefix.REGISTER) && isExistPhone) {
            log.warn("중복된 전화번호로 인한 회원가입 요청 실패: {}", req.to());
            throw new GlobalErrorException(AccountErrorCode.DUPLICATE_PHONE_ERROR);
        } else if (prefix.equals(SmsPrefix.UID) && !isExistPhone) {
            log.warn("DB에 존재하지 않는 전화번호로 인한 SMS 인증 요청 실패: {}", req.to());
            throw new GlobalErrorException(AccountErrorCode.NOT_FOUND_PHONE_ERROR);
        } else if (prefix.equals(SmsPrefix.PASSWORD)) {
            if (!isExistPhone) {
                log.warn("DB에 존재하지 않는 전화번호로 인한 SMS 인증 요청 실패: {}", req.to());
                throw new GlobalErrorException(AccountErrorCode.NOT_FOUND_PHONE_ERROR);
            } else if (!memberSearchService.isExistByPhoneAndUid(req.to(), req.uid())) {
                log.warn("등록된 유저 전화번호와 다른 전화번호 입력: {}", req.to());
                throw new GlobalErrorException(AccountErrorCode.MISSMATCH_PHONE_AND_UID_ERROR);
            }
        }
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
