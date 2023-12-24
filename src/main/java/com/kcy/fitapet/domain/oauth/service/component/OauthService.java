package com.kcy.fitapet.domain.oauth.service.component;

import com.kcy.fitapet.domain.member.domain.Member;
import com.kcy.fitapet.domain.member.exception.SmsErrorCode;
import com.kcy.fitapet.domain.member.service.module.MemberSaveService;
import com.kcy.fitapet.domain.member.service.module.MemberSearchService;
import com.kcy.fitapet.domain.member.type.RoleType;
import com.kcy.fitapet.domain.oauth.domain.OauthAccount;
import com.kcy.fitapet.domain.oauth.dto.OauthSignUpReq;
import com.kcy.fitapet.domain.oauth.exception.OauthException;
import com.kcy.fitapet.domain.oauth.service.module.OauthApplicationConfigHelper;
import com.kcy.fitapet.domain.oauth.service.module.OauthClientHelper;
import com.kcy.fitapet.domain.oauth.service.module.OauthSearchService;
import com.kcy.fitapet.domain.oauth.type.ProviderType;
import com.kcy.fitapet.global.common.redis.oauth.OIDCTokenService;
import com.kcy.fitapet.global.common.redis.sms.SmsCertificationService;
import com.kcy.fitapet.global.common.redis.sms.SmsPrefix;
import com.kcy.fitapet.global.common.response.exception.GlobalErrorException;
import com.kcy.fitapet.global.common.security.jwt.JwtUtil;
import com.kcy.fitapet.global.common.security.jwt.dto.Jwt;
import com.kcy.fitapet.global.common.security.jwt.dto.JwtUserInfo;
import com.kcy.fitapet.global.common.security.jwt.dto.SmsAuthInfo;
import com.kcy.fitapet.global.common.security.oauth.OauthApplicationConfig;
import com.kcy.fitapet.global.common.security.oauth.OauthClient;
import com.kcy.fitapet.global.common.security.oauth.OauthOIDCHelper;
import com.kcy.fitapet.global.common.security.oauth.dto.OIDCDecodePayload;
import com.kcy.fitapet.global.common.security.oauth.dto.OIDCPublicKeyResponse;
import com.kcy.fitapet.global.common.util.sms.SmsProvider;
import com.kcy.fitapet.global.common.util.sms.dto.SensInfo;
import com.kcy.fitapet.global.common.util.sms.dto.SmsReq;
import com.kcy.fitapet.global.common.util.sms.dto.SmsRes;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class OauthService {
    private final OauthSearchService oauthSearchService;
    private final MemberSaveService memberSaveService;
    private final MemberSearchService memberSearchService;

    private final OauthOIDCHelper oauthOIDCHelper;
    private final OauthApplicationConfigHelper oauthApplicationConfigHelper;
    private final OauthClientHelper oauthClientHelper;

    private final JwtUtil jwtUtil;
    private final OIDCTokenService oidcTokenService;
    private final SmsProvider smsProvider;
    private final SmsCertificationService smsCertificationService;

    @Transactional
    public Jwt signInByOIDC(Long id, String idToken, ProviderType provider, String nonce) {
        OIDCDecodePayload payload = getPayload(provider, idToken, nonce);
        isValidRequestId(id, Long.parseLong(payload.sub()));

        if (oauthSearchService.isExistMember(id, provider)) {
            Member member = oauthSearchService.findMemberByOauthIdAndProvider(id, provider);
            return generateToken(JwtUserInfo.from(member));
        } else {
            oidcTokenService.saveOIDCToken(idToken, provider, id);
            return null;
        }
    }

    @Transactional
    public Jwt signUpByOIDC(Long id, ProviderType provider, OauthSignUpReq req) {
        String idToken = oidcTokenService.findOIDCToken(req.idToken()).getToken();
        OIDCDecodePayload payload = getPayload(provider, idToken, req.nonce());

        Member member = (memberSearchService.isExistByPhone(req.phone()))
                ? memberSearchService.findByPhone(req.phone())
                : Member.builder().uid(req.uid()).name(req.name())
                .phone(req.phone()).isOauth(Boolean.TRUE).role(RoleType.USER).build();
        memberSaveService.saveMember(member);
        OauthAccount oauthAccount = OauthAccount.of(id, provider, payload.email(), member);

        log.info("success oauth signup member id : {} - oauth id : {} [provider: {}]",
                member.getId(), oauthAccount.getOauthId(), oauthAccount.getProvider());
        return generateToken(JwtUserInfo.from(member));
    }

    @Transactional
    public SmsRes sendCode(SmsReq dto, Long id, ProviderType provider, SmsPrefix prefix) {
        SensInfo smsInfo = smsProvider.sendCodeByPhoneNumber(dto);

        smsCertificationService.saveSmsAuthToken(dto.to(), smsInfo.code(), prefix);
        LocalDateTime expireTime = smsCertificationService.getExpiredTime(dto.to(), prefix);
        log.info("인증번호 만료 시간: {}", expireTime);
        return SmsRes.of(dto.to(), smsInfo.requestTime(), expireTime);
    }

    @Transactional
    public String checkCertificationNumber(SmsReq req, Long id, String code) {
        if (!smsCertificationService.isCorrectCode(req.to(), code, SmsPrefix.REGISTER)) {
            log.warn("인증번호 불일치 -> 사용자 입력 인증 번호 : {}", code);
            throw new GlobalErrorException(SmsErrorCode.INVALID_AUTH_CODE);
        }

        String token = jwtUtil.generateSmsOauthToken(SmsAuthInfo.of(id, req.to()));
        smsCertificationService.saveSmsAuthToken(req.to(), token, SmsPrefix.OAUTH);

        return token;
    }

    private OIDCDecodePayload getPayload(ProviderType provider, String idToken, String nonce) {
        OauthClient oauthClient = oauthClientHelper.getOauthClient(provider);
        OauthApplicationConfig oauthApplicationConfig = oauthApplicationConfigHelper.getOauthApplicationConfig(provider);
        OIDCPublicKeyResponse oidcPublicKeyResponse = oauthClient.getOIDCPublicKey();

        return oauthOIDCHelper.getPayloadFromIdToken(
                idToken, oauthApplicationConfig.getAuthorizationUri(),
                oauthApplicationConfig.getClientId(), nonce, oidcPublicKeyResponse);
    }

    /**
     * 요청한 id와 idToken의 sub가 일치하는지 확인
     */
    private void isValidRequestId(Long id, Long sub) {
        if (!id.equals(sub)) {
            throw new GlobalErrorException(OauthException.INVALID_OAUTH_ID);
        }
    }

    private Jwt generateToken(JwtUserInfo jwtUserInfo) {
        return Jwt.builder()
                .accessToken(jwtUtil.generateAccessToken(jwtUserInfo))
                .refreshToken(jwtUtil.generateRefreshToken(jwtUserInfo))
                .build();
    }
}
