package kr.co.fitapet.api.apis.auth.usecase;

import kr.co.fitapet.api.common.security.jwt.JwtProvider;
import kr.co.fitapet.api.common.security.jwt.dto.Jwt;
import kr.co.fitapet.api.common.security.jwt.dto.JwtUserInfo;
import kr.co.fitapet.api.common.security.jwt.qualifier.AccessTokenQualifier;
import kr.co.fitapet.api.common.security.jwt.qualifier.RefreshTokenQualifier;
import kr.co.fitapet.api.common.security.jwt.qualifier.SmsAuthTokenQualifier;
import kr.co.fitapet.api.common.security.jwt.strategy.AccessTokenProvider;
import kr.co.fitapet.domain.common.redis.forbidden.ForbiddenTokenService;
import kr.co.fitapet.domain.common.redis.refresh.RefreshToken;
import kr.co.fitapet.domain.common.redis.refresh.RefreshTokenService;
import kr.co.fitapet.domain.domains.member.service.MemberSaveService;
import kr.co.fitapet.domain.domains.member.service.MemberSearchService;
import kr.co.fitapet.infra.client.sms.snes.SmsProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

import static java.util.Calendar.ZONE_OFFSET;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberAuthUseCase {
    private final MemberSearchService memberSearchService;
    private final MemberSaveService memberSaveService;

    private final RefreshTokenService refreshTokenService;
    private final ForbiddenTokenService forbiddenTokenService;

    private final SmsRedisHelper smsRedisHelper;

    private final SmsProvider smsProvider;

    @AccessTokenQualifier
    private final JwtProvider accessTokenProvider;
    @RefreshTokenQualifier
    private final JwtProvider refreshTokenProvider;
    @SmsAuthTokenQualifier
    private final JwtProvider smsAuthTokenProvider;

    private final PasswordEncoder bCryptPasswordEncoder;

    @Transactional
    public Pair<Long, Jwt> register(String requestSmsAccessToken, SignUpReq dto) {
        String accessToken = smsAuthTokenProvider.resolveToken(requestSmsAccessToken);

        if (forbiddenTokenService.isForbidden(accessToken))
            throw new GlobalErrorException(AuthErrorCode.FORBIDDEN_ACCESS_TOKEN);

        JwtSubInfo jwtSubInfo = smsAuthTokenProvider.getSubInfoFromToken(accessToken);
        smsRedisHelper.removeCode(jwtSubInfo.phoneNumber(), SmsPrefix.REGISTER);

        Member requestMember = dto.toEntity(jwtSubInfo.phoneNumber());
        requestMember.encodePassword(bCryptPasswordEncoder);
        validateMember(requestMember);

        Member registeredMember = memberSaveService.saveMember(requestMember);

        forbiddenTokenService.register(
                AccessToken.of(accessToken, jwtSubInfo.id(),
                        jwtMapper.getProvider(SMS_AUTH_TOKEN).getExpiryDate(accessToken), false)
        );

        return Pair.of(registeredMember.getId(), generateToken(JwtUserInfo.from(registeredMember)));
    }

    @Transactional
    public Pair<Long, Jwt> login(SignInReq dto) {
        Member member = memberSearchService.findByUid(dto.uid());
        if (!member.checkPassword(dto.password(), bCryptPasswordEncoder))
            throw new GlobalErrorException(AccountErrorCode.NOT_MATCH_PASSWORD_ERROR);

        return Pair.of(member.getId(), generateToken(JwtUserInfo.from(member)));
    }

    @Transactional
    public void logout(AccessToken requestAccessToken, String requestRefreshToken) {
        forbiddenTokenService.register(requestAccessToken);

        if (!StringUtils.hasText(requestRefreshToken))
            refreshTokenService.logout(requestRefreshToken);
    }

    @Transactional
    public Jwt refresh(String requestRefreshToken) {
        RefreshToken refreshToken = refreshTokenService.refresh(requestRefreshToken);

        Long memberId = refreshToken.getUserId();
        JwtUserInfo dto = JwtUserInfo.from(memberSearchService.findById(memberId));
        String accessToken = jwtMapper.getProvider(ACCESS_TOKEN).generateToken(dto);

        return Jwt.of(accessToken, refreshToken.getToken());
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
    public Jwt checkCodeForRegister(SmsReq smsReq, String requestCode) {
        if (!smsRedisHelper.isCorrectCode(smsReq.to(), requestCode, SmsPrefix.REGISTER)) {
            log.warn("인증번호 불일치 -> 사용자 입력 인증 번호 : {}", requestCode);
            throw new GlobalErrorException(SmsErrorCode.INVALID_AUTH_CODE);
        }
        smsRedisHelper.removeCode(smsReq.to(), SmsPrefix.REGISTER);

        if (memberSearchService.isExistByPhone(smsReq.to())) {
            Member member = memberSearchService.findByPhone(smsReq.to());
            if (member.getIsOauth().equals(Boolean.TRUE)) {
                member.updateOathToOriginAccount();
                return generateToken(JwtUserInfo.from(member));
            }
        }

        return Jwt.of(jwtMapper.getProvider(SMS_AUTH_TOKEN).generateToken(SmsAuthInfo.of(1L, smsReq.to())), null);
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
            if (!memberSearchService.findByPhone(req.to()).getIsOauth()) {
                log.warn("중복된 전화번호로 인한 회원가입 요청 실패: {}", req.to());
                throw new GlobalErrorException(AccountErrorCode.DUPLICATE_PHONE_ERROR);
            }
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
        if (memberSearchService.isExistByUidOrPhone(member.getUid(), member.getPhone()))
            throw new GlobalErrorException(AccountErrorCode.DUPLICATE_USER_INFO_ERROR);
    }

    private Jwt generateToken(JwtUserInfo jwtUserInfo) {
        String accessToken = accessTokenProvider.generateToken(jwtUserInfo);
        String refreshToken = refreshTokenProvider.generateToken(jwtUserInfo);
        refreshTokenService.issueRefreshToken(
                RefreshToken.of(jwtUserInfo.id(), refreshToken, refreshTokenProvider.getExpiryDate(refreshToken).toEpochSecond(ZoneOffset.ofHours(ZONE_OFFSET))));
        log.debug("accessToken : {}, refreshToken : {}", accessToken, refreshToken);

        return Jwt.of(accessToken, refreshToken);
    }
}
