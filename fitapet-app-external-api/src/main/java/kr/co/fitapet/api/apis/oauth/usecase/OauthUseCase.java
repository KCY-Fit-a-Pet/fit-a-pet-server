package kr.co.fitapet.api.apis.oauth.usecase;

import kr.co.fitapet.api.apis.auth.dto.SmsRes;
import kr.co.fitapet.api.apis.auth.mapper.JwtMapper;
import kr.co.fitapet.api.apis.auth.mapper.SmsRedisMapper;
import kr.co.fitapet.api.apis.oauth.dto.OauthSmsReq;
import kr.co.fitapet.api.apis.oauth.helper.OauthOIDCHelper;
import kr.co.fitapet.api.common.security.jwt.consts.JwtType;
import kr.co.fitapet.api.common.security.jwt.dto.Jwt;
import kr.co.fitapet.api.common.security.jwt.dto.JwtSubInfo;
import kr.co.fitapet.api.common.security.jwt.dto.JwtUserInfo;
import kr.co.fitapet.api.common.security.jwt.dto.SmsOauthInfo;
import kr.co.fitapet.api.common.security.jwt.exception.AuthErrorCode;
import kr.co.fitapet.api.common.security.jwt.exception.AuthErrorException;
import kr.co.fitapet.common.annotation.UseCase;
import kr.co.fitapet.common.execption.GlobalErrorException;
import kr.co.fitapet.domain.common.redis.oauth.OIDCTokenService;
import kr.co.fitapet.domain.common.redis.sms.type.SmsPrefix;
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
import kr.co.fitapet.api.apis.oauth.mapper.OauthClientMapper;
import kr.co.fitapet.infra.client.oauth.dto.OIDCDecodePayload;
import kr.co.fitapet.infra.client.oauth.dto.OIDCPublicKeyResponse;
import kr.co.fitapet.infra.client.oauth.OauthApplicationConfig;
import kr.co.fitapet.api.apis.oauth.mapper.OauthApplicationConfigMapper;
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
    private final OauthClientMapper oauthClientMapper;

    private final JwtMapper jwtMapper;
    private final SmsRedisMapper smsRedisMapper;

    private final OIDCTokenService oidcTokenService;
    private final SmsProvider smsProvider;

    @Transactional
    public Optional<Pair<Long, Jwt>> signInByOIDC(String id, String idToken, ProviderType provider, String nonce) {
        OIDCDecodePayload payload = getPayload(provider, idToken, nonce);
        log.info("payload : {}", payload);
        isValidRequestId(id, payload.sub());

        if (oauthSearchService.isExistMember(id, provider)) {
            Member member = oauthSearchService.findMemberByOauthIdAndProvider(id, provider);
            return Optional.of(Pair.of(member.getId(), jwtMapper.login(JwtUserInfo.from(member))));
        } else {
            oidcTokenService.saveOIDCToken(idToken, provider, id);
            return Optional.empty();
        }
    }

    @Transactional
    public Pair<Long, Jwt> signUpByOIDC(String id, ProviderType provider, String requestOauthAccessToken, OauthSignUpReq req) {
        String smsOauthToken = jwtMapper.resolveToken(requestOauthAccessToken, JwtType.SMS_OAUTH_TOKEN);
        JwtSubInfo subs = jwtMapper.getSubInfoFromToken(smsOauthToken, JwtType.SMS_OAUTH_TOKEN);
        String phone = getPhoneByTopic(subs.phoneNumber());

        validateToken(smsOauthToken, subs.phoneNumber(), provider);

        String idToken = oidcTokenService.findOIDCToken(req.idToken()).getToken();
        OIDCDecodePayload payload = getPayload(provider, idToken, req.nonce());

        Member member = Member.builder().uid(req.uid()).name(req.name())
                .phone(phone).isOauth(Boolean.TRUE).role(RoleType.USER).build();
        memberSaveService.saveMember(member);

        OauthAccount oauthAccount = OauthAccount.of(id, provider, payload.email());
        oauthAccount.updateMember(member);
        oidcTokenService.deleteOIDCToken(req.idToken());

        jwtMapper.ban(smsOauthToken, JwtType.SMS_OAUTH_TOKEN);
        log.info("success oauth signup member id : {} - oauth id : {} [provider: {}]",
                member.getId(), oauthAccount.getOauthId(), oauthAccount.getProvider());
        return Pair.of(member.getId(), jwtMapper.login(JwtUserInfo.from(member)));
    }

    @Transactional
    public SmsRes sendCode(OauthSmsReq dto, ProviderType provider) {
        SnesDto.SensInfo smsInfo = smsProvider.sendCodeByPhoneNumber(SnesDto.Request.of(dto.to()));
        String key = makeTopic(dto.to(), provider);

        smsRedisMapper.saveSmsAuthToken(key, smsInfo.code(), SmsPrefix.OAUTH);
        LocalDateTime expireTime = smsRedisMapper.getExpiredTime(key, SmsPrefix.OAUTH);
        log.info("인증번호 만료 시간: {}", expireTime);
        return SmsRes.of(dto.to(), smsInfo.requestTime(), expireTime);
    }

    @Transactional
    public Pair<Long, Jwt> checkCertificationNumber(OauthSmsReq req, String id, String code, ProviderType provider) {
        String key = makeTopic(req.to(), provider);
        log.info("key: {}", key);
        if (!smsRedisMapper.isCorrectCode(key, code, SmsPrefix.OAUTH)) {
            log.warn("인증번호 불일치 -> 사용자 입력 인증 번호 : {}", code);
            throw new GlobalErrorException(SmsErrorCode.INVALID_AUTH_CODE);
        }
        smsRedisMapper.removeCode(key, SmsPrefix.OAUTH);

        if (memberSearchService.isExistByPhone(req.to())) {
            Member member = memberSearchService.findByPhone(req.to());
            String idToken = oidcTokenService.findOIDCToken(req.idToken()).getToken();
            OIDCDecodePayload payload = getPayload(provider, idToken, req.nonce());
            OauthAccount oauthAccount = OauthAccount.of(id, provider, payload.email());
            oauthAccount.updateMember(member);
            oidcTokenService.deleteOIDCToken(req.idToken());

            return Pair.of(member.getId(), jwtMapper.login(JwtUserInfo.from(member)));
        }

        return Pair.of(0L, Jwt.of(jwtMapper.generateToken(SmsOauthInfo.of(id, key), JwtType.SMS_OAUTH_TOKEN), null));
    }

    /**
     * idToken을 통해 payload를 가져온다.
     */
    private OIDCDecodePayload getPayload(ProviderType provider, String idToken, String nonce) {
        OauthApplicationConfig oauthApplicationConfig = oauthClientMapper.getOauthApplicationConfig(Provider.valueOf(provider.name()));
        OIDCPublicKeyResponse oidcPublicKeyResponse = oauthClientMapper.getPublicKeyResponse(Provider.valueOf(provider.name()));

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
        if (jwtMapper.isForbidden(accessToken))
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
}
