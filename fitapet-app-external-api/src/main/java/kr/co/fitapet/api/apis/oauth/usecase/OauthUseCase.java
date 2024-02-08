package kr.co.fitapet.api.apis.oauth.usecase;

import kr.co.fitapet.api.apis.auth.dto.SmsRes;
import kr.co.fitapet.api.apis.oauth.dto.OauthSmsReq;
import kr.co.fitapet.api.apis.oauth.helper.OauthOIDCHelper;
import kr.co.fitapet.api.common.security.jwt.AuthConstants;
import kr.co.fitapet.api.common.security.jwt.JwtProvider;
import kr.co.fitapet.api.common.security.jwt.dto.Jwt;
import kr.co.fitapet.api.common.security.jwt.dto.JwtSubInfo;
import kr.co.fitapet.api.common.security.jwt.dto.JwtUserInfo;
import kr.co.fitapet.api.common.security.jwt.dto.SmsOauthInfo;
import kr.co.fitapet.api.common.security.jwt.exception.AuthErrorCode;
import kr.co.fitapet.api.common.security.jwt.exception.AuthErrorException;
import kr.co.fitapet.api.common.security.jwt.qualifier.AccessTokenQualifier;
import kr.co.fitapet.api.common.security.jwt.qualifier.RefreshTokenQualifier;
import kr.co.fitapet.api.common.security.jwt.qualifier.SmsOauthTokenQualifier;
import kr.co.fitapet.common.annotation.UseCase;
import kr.co.fitapet.common.execption.GlobalErrorException;
import kr.co.fitapet.domain.common.redis.forbidden.ForbiddenTokenService;
import kr.co.fitapet.domain.common.redis.oauth.OIDCTokenService;
import kr.co.fitapet.domain.common.redis.sms.provider.SmsRedisProvider;
import kr.co.fitapet.domain.common.redis.sms.qualify.SmsRegisterQualifier;
import kr.co.fitapet.domain.common.redis.sms.type.SmsPrefix;
import kr.co.fitapet.domain.domains.member.domain.AccessToken;
import kr.co.fitapet.domain.domains.member.domain.Member;
import kr.co.fitapet.domain.domains.member.service.MemberSaveService;
import kr.co.fitapet.domain.domains.member.service.MemberSearchService;
import kr.co.fitapet.domain.domains.member.type.RoleType;
import kr.co.fitapet.domain.domains.oauth.domain.OauthAccount;
import kr.co.fitapet.domain.domains.oauth.dto.OauthSignUpReq;
import kr.co.fitapet.domain.domains.oauth.exception.OauthException;
import kr.co.fitapet.domain.domains.oauth.service.OauthSearchService;
import kr.co.fitapet.domain.domains.oauth.type.ProviderType;
import kr.co.fitapet.infra.client.oauth.OauthClient;
import kr.co.fitapet.infra.client.oauth.OauthClientMapper;
import kr.co.fitapet.infra.client.oauth.dto.OIDCDecodePayload;
import kr.co.fitapet.infra.client.oauth.dto.OIDCPublicKeyResponse;
import kr.co.fitapet.infra.client.oauth.environment.OauthApplicationConfig;
import kr.co.fitapet.infra.client.oauth.environment.OauthApplicationConfigMapper;
import kr.co.fitapet.infra.client.oauth.type.Provider;
import kr.co.fitapet.infra.client.sms.snes.SmsProvider;
import kr.co.fitapet.infra.client.sms.snes.dto.SnesDto;
import kr.co.fitapet.infra.client.sms.snes.exception.SmsErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@UseCase
@RequiredArgsConstructor
@Slf4j
public class OauthUseCase {
    private final OauthSearchService oauthSearchService;
    private final MemberSaveService memberSaveService;
    private final MemberSearchService memberSearchService;

    private final OauthOIDCHelper oauthOIDCHelper;
    private final OauthApplicationConfigMapper oauthApplicationConfigMapper;
    private final OauthClientMapper oauthClientMapper;

    @AccessTokenQualifier
    private final JwtProvider accessTokenProvider;
    @RefreshTokenQualifier
    private final JwtProvider refreshTokenProvider;
    @SmsOauthTokenQualifier
    private final JwtProvider smsOauthTokenProvider;
    private final ForbiddenTokenService forbiddenTokenService;

    private final OIDCTokenService oidcTokenService;
    private final SmsProvider smsProvider;
    @SmsRegisterQualifier
    private final SmsRedisProvider smsRedisHelper;

    @Transactional
    public Optional<Pair<Long, Jwt>> signInByOIDC(String id, String idToken, ProviderType provider, String nonce) {
        OIDCDecodePayload payload = getPayload(provider, idToken, nonce);
        log.info("payload : {}", payload);
        isValidRequestId(id, payload.sub());

        if (oauthSearchService.isExistMember(id, provider)) {
            Member member = oauthSearchService.findMemberByOauthIdAndProvider(id, provider);
            return Optional.of(Pair.of(member.getId(), generateToken(JwtUserInfo.from(member))));
        } else {
            oidcTokenService.saveOIDCToken(idToken, provider, id);
            return Optional.empty();
        }
    }

    @Transactional
    public Pair<Long, Jwt> signUpByOIDC(String id, ProviderType provider, String requestOauthAccessToken, OauthSignUpReq req) {
        String accessToken = smsOauthTokenProvider.resolveToken(requestOauthAccessToken);
        JwtSubInfo subs = smsOauthTokenProvider.getSubInfoFromToken(accessToken);
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
                AccessToken.of(accessToken, subs.id(), smsOauthTokenProvider.getExpiryDate(accessToken))
        );

        log.info("success oauth signup member id : {} - oauth id : {} [provider: {}]",
                member.getId(), oauthAccount.getOauthId(), oauthAccount.getProvider());
        return Pair.of(member.getId(), generateToken(JwtUserInfo.from(member)));
    }

    @Transactional
    public SmsRes sendCode(OauthSmsReq dto, ProviderType provider) {
        SnesDto.SensInfo smsInfo = smsProvider.sendCodeByPhoneNumber(SnesDto.Request.of(dto.to()));
        String key = makeTopic(dto.to(), provider);

        smsRedisHelper.saveSmsAuthToken(key, smsInfo.code(), SmsPrefix.OAUTH);
        LocalDateTime expireTime = smsRedisHelper.getExpiredTime(key, SmsPrefix.OAUTH);
        log.info("인증번호 만료 시간: {}", expireTime);
        return SmsRes.of(dto.to(), smsInfo.requestTime(), expireTime);
    }

    @Transactional
    public Pair<Long, Jwt> checkCertificationNumber(OauthSmsReq req, String id, String code, ProviderType provider) {
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

            return Pair.of(member.getId(), generateToken(JwtUserInfo.from(member)));
        }

        return Pair.of(0L, Jwt.of(smsOauthTokenProvider.generateToken(SmsOauthInfo.of(id, key)), null));
    }

    /**
     * idToken을 통해 payload를 가져온다.
     */
    private OIDCDecodePayload getPayload(ProviderType provider, String idToken, String nonce) {
        OauthClient oauthClient = oauthClientMapper.getOauthClient(Provider.valueOf(provider.name()));
        OauthApplicationConfig oauthApplicationConfig = oauthApplicationConfigMapper.getOauthApplicationConfig(Provider.valueOf(provider.name()));
        OIDCPublicKeyResponse oidcPublicKeyResponse = oauthClient.getOIDCPublicKey();

        return oauthOIDCHelper.getPayloadFromIdToken(
                idToken, oauthApplicationConfig.getJwksUri(),
                oauthApplicationConfig.getClientSecret(), nonce, oidcPublicKeyResponse);
    }

    /**
     * 요청한 id와 idToken의 sub가 일치하는지 확인
     */
    private void isValidRequestId(String id, String sub) {
        if (!id.equals(sub)) {
            throw new GlobalErrorException(OauthException.INVALID_OAUTH_ID);
        }
    }

    private String makeTopic(String phoneNumber, ProviderType provider) {
        return provider.name() + "_" + phoneNumber;
    }

    private void validateToken(String accessToken, String value, ProviderType provider) {
        if (forbiddenTokenService.isForbidden(accessToken))
            throw new AuthErrorException(AuthErrorCode.FORBIDDEN_ACCESS_TOKEN, "forbidden access token");

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
                .accessToken(accessTokenProvider.generateToken(jwtSubInfo))
                .refreshToken(refreshTokenProvider.generateToken(jwtSubInfo))
                .build();
    }
}
