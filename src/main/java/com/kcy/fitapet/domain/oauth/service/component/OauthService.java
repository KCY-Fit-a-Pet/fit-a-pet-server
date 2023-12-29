package com.kcy.fitapet.domain.oauth.service.component;

import com.kcy.fitapet.domain.member.domain.Member;
import com.kcy.fitapet.domain.member.domain.NotificationSetting;
import com.kcy.fitapet.domain.member.exception.SmsErrorCode;
import com.kcy.fitapet.domain.member.service.module.MemberSaveService;
import com.kcy.fitapet.domain.member.service.module.MemberSearchService;
import com.kcy.fitapet.domain.member.type.RoleType;
import com.kcy.fitapet.domain.oauth.domain.OauthAccount;
import com.kcy.fitapet.domain.oauth.dto.OauthSignUpReq;
import com.kcy.fitapet.domain.oauth.dto.OauthSmsReq;
import com.kcy.fitapet.domain.oauth.exception.OauthException;
import com.kcy.fitapet.domain.oauth.service.module.OauthSearchService;
import com.kcy.fitapet.domain.oauth.type.ProviderType;
import com.kcy.fitapet.global.common.redis.forbidden.ForbiddenTokenService;
import com.kcy.fitapet.global.common.redis.oauth.OIDCTokenService;
import com.kcy.fitapet.global.common.redis.sms.SmsRedisHelper;
import com.kcy.fitapet.global.common.redis.sms.type.SmsPrefix;
import com.kcy.fitapet.global.common.resolver.access.AccessToken;
import com.kcy.fitapet.global.common.response.exception.GlobalErrorException;
import com.kcy.fitapet.global.common.security.jwt.AuthConstants;
import com.kcy.fitapet.global.common.security.jwt.JwtProviderMapper;
import com.kcy.fitapet.global.common.security.jwt.dto.Jwt;
import com.kcy.fitapet.global.common.security.jwt.dto.JwtSubInfo;
import com.kcy.fitapet.global.common.security.jwt.dto.JwtUserInfo;
import com.kcy.fitapet.global.common.security.jwt.dto.SmsAuthInfo;
import com.kcy.fitapet.global.common.security.jwt.exception.AuthErrorCode;
import com.kcy.fitapet.global.common.security.oauth.*;
import com.kcy.fitapet.global.common.security.oauth.dto.OIDCDecodePayload;
import com.kcy.fitapet.global.common.security.oauth.dto.OIDCPublicKeyResponse;
import com.kcy.fitapet.global.common.util.sms.SmsProvider;
import com.kcy.fitapet.global.common.util.sms.dto.SensInfo;
import com.kcy.fitapet.global.common.util.sms.dto.SmsRes;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

import static com.kcy.fitapet.global.common.security.jwt.AuthConstants.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class OauthService {
    private final OauthSearchService oauthSearchService;
    private final MemberSaveService memberSaveService;
    private final MemberSearchService memberSearchService;

    private final OauthOIDCHelper oauthOIDCHelper;
    private final OauthApplicationConfigMapper oauthApplicationConfigMapper;
    private final OauthClientMapper oauthClientMapper;

    private final JwtProviderMapper jwtMapper;
    private final ForbiddenTokenService forbiddenTokenService;

    private final OIDCTokenService oidcTokenService;
    private final SmsProvider smsProvider;
    private final SmsRedisHelper smsRedisHelper;

    @Transactional
    public Optional<Jwt> signInByOIDC(Long id, String idToken, ProviderType provider, String nonce) {
        OIDCDecodePayload payload = getPayload(provider, idToken, nonce);
        log.info("payload : {}", payload);
        isValidRequestId(id, Long.parseLong(payload.sub()));

        if (oauthSearchService.isExistMember(id, provider)) {
            Member member = oauthSearchService.findMemberByOauthIdAndProvider(id, provider);
            return Optional.of(generateToken(JwtUserInfo.from(member)));
        } else {
            oidcTokenService.saveOIDCToken(idToken, provider, id);
            return Optional.empty();
        }
    }

    @Transactional
    public Jwt signUpByOIDC(Long id, ProviderType provider, String requestOauthAccessToken, OauthSignUpReq req) {
        String accessToken = jwtMapper.getProvider(AuthConstants.SMS_OAUTH_TOKEN).resolveToken(requestOauthAccessToken);
        JwtSubInfo subs = jwtMapper.getProvider(AuthConstants.SMS_OAUTH_TOKEN).getSubInfoFromToken(accessToken);
        String phone = getPhoneByTopic(subs.phoneNumber());

        validateToken(accessToken, subs.phoneNumber(), provider);

        String idToken = oidcTokenService.findOIDCToken(req.idToken()).getToken();
        OIDCDecodePayload payload = getPayload(provider, idToken, req.nonce());

        Member member = Member.builder().uid(req.uid()).name(req.name())
                .phone(phone).isOauth(Boolean.TRUE).role(RoleType.USER).build();
        memberSaveService.saveMember(member);
        OauthAccount oauthAccount = OauthAccount.of(id, provider, payload.email());
        oauthAccount.updateMember(member);
        oidcTokenService.deleteOIDCToken(req.idToken());

        forbiddenTokenService.register(
                AccessToken.of(accessToken, subs.id(),
                        jwtMapper.getProvider(AuthConstants.SMS_OAUTH_TOKEN).getExpiryDate(accessToken), false)
        );

        log.info("success oauth signup member id : {} - oauth id : {} [provider: {}]",
                member.getId(), oauthAccount.getOauthId(), oauthAccount.getProvider());
        return generateToken(JwtUserInfo.from(member));
    }

    @Transactional
    public SmsRes sendCode(OauthSmsReq dto, ProviderType provider) {
        SensInfo smsInfo = smsProvider.sendCodeByPhoneNumber(dto.toSmsReq());
        String key = makeTopic(dto.to(), provider);

        smsRedisHelper.saveSmsAuthToken(key, smsInfo.code(), SmsPrefix.OAUTH);
        LocalDateTime expireTime = smsRedisHelper.getExpiredTime(key, SmsPrefix.OAUTH);
        log.info("인증번호 만료 시간: {}", expireTime);
        return SmsRes.of(dto.to(), smsInfo.requestTime(), expireTime);
    }

    @Transactional
    public Jwt checkCertificationNumber(OauthSmsReq req, Long id, String code, ProviderType provider) {
        String key = makeTopic(req.to(), provider);
        log.info("key: {}", key);
        if (!smsRedisHelper.isCorrectCode(key, code, SmsPrefix.OAUTH)) {
            log.warn("인증번호 불일치 -> 사용자 입력 인증 번호 : {}", code);
            throw new GlobalErrorException(SmsErrorCode.INVALID_AUTH_CODE);
        }
        smsRedisHelper.removeCode(key, SmsPrefix.OAUTH);

        if (memberSearchService.isExistByPhone(req.to())) {
            Member member = memberSearchService.findByPhone(req.to());
            String idToken = oidcTokenService.findOIDCToken(req.idToken()).getToken();
            OIDCDecodePayload payload = getPayload(provider, idToken, req.nonce());
            OauthAccount oauthAccount = OauthAccount.of(id, provider, payload.email());
            oauthAccount.updateMember(member);
            oidcTokenService.deleteOIDCToken(req.idToken());

            return generateToken(JwtUserInfo.from(member));
        }

        return Jwt.of(jwtMapper.getProvider(SMS_OAUTH_TOKEN).generateToken(SmsAuthInfo.of(id, key)), null);
    }

    /**
     * idToken을 통해 payload를 가져온다.
     */
    private OIDCDecodePayload getPayload(ProviderType provider, String idToken, String nonce) {
        OauthClient oauthClient = oauthClientMapper.getOauthClient(provider);
        OauthApplicationConfig oauthApplicationConfig = oauthApplicationConfigMapper.getOauthApplicationConfig(provider);
        OIDCPublicKeyResponse oidcPublicKeyResponse = oauthClient.getOIDCPublicKey();

        return oauthOIDCHelper.getPayloadFromIdToken(
                idToken, oauthApplicationConfig.getJwksUri(),
                oauthApplicationConfig.getClientSecret(), nonce, oidcPublicKeyResponse);
    }

    /**
     * 요청한 id와 idToken의 sub가 일치하는지 확인
     */
    private void isValidRequestId(Long id, Long sub) {
        if (!id.equals(sub)) {
            throw new GlobalErrorException(OauthException.INVALID_OAUTH_ID);
        }
    }

    private String makeTopic(String phoneNumber, ProviderType provider) {
        return provider.name() + "_" + phoneNumber;
    }

    private void validateToken(String accessToken, String value, ProviderType provider) {
        if (forbiddenTokenService.isForbidden(accessToken))
            throw new GlobalErrorException(AuthErrorCode.FORBIDDEN_ACCESS_TOKEN);

        ProviderType tokenProvider = getProviderByTopic(value);
        if (!provider.equals(tokenProvider))
            throw new GlobalErrorException(OauthException.INVALID_OAUTH_PROVIDER);
    }

    private ProviderType getProviderByTopic(String topic) {
        return ProviderType.valueOf(topic.split("_")[0].toUpperCase());
    }

    private String getPhoneByTopic(String topic) {
        return topic.split("_")[1];
    }

    private Jwt generateToken(JwtSubInfo jwtSubInfo) {
        return Jwt.builder()
                .accessToken(jwtMapper.getProvider(ACCESS_TOKEN).generateToken(jwtSubInfo))
                .refreshToken(jwtMapper.getProvider(REFRESH_TOKEN).generateToken(jwtSubInfo))
                .build();
    }
}
