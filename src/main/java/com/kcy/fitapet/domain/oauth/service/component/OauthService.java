package com.kcy.fitapet.domain.oauth.service.component;

import com.kcy.fitapet.domain.member.domain.Member;
import com.kcy.fitapet.domain.oauth.exception.OauthException;
import com.kcy.fitapet.domain.oauth.service.module.OauthApplicationConfigHelper;
import com.kcy.fitapet.domain.oauth.service.module.OauthClientHelper;
import com.kcy.fitapet.domain.oauth.service.module.OauthSearchService;
import com.kcy.fitapet.domain.oauth.type.ProviderType;
import com.kcy.fitapet.global.common.response.exception.GlobalErrorException;
import com.kcy.fitapet.global.common.security.jwt.JwtUtil;
import com.kcy.fitapet.global.common.security.jwt.dto.Jwt;
import com.kcy.fitapet.global.common.security.jwt.dto.JwtUserInfo;
import com.kcy.fitapet.global.common.security.oauth.OauthApplicationConfig;
import com.kcy.fitapet.global.common.security.oauth.OauthClient;
import com.kcy.fitapet.global.common.security.oauth.OauthOIDCHelper;
import com.kcy.fitapet.global.common.security.oauth.dto.OIDCDecodePayload;
import com.kcy.fitapet.global.common.security.oauth.dto.OIDCPublicKeyResponse;
import com.kcy.fitapet.global.common.security.oauth.kakao.KakaoOauthClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class OauthService {
    private final OauthSearchService oauthSearchService;

    private final OauthOIDCHelper oauthOIDCHelper;
    private final OauthApplicationConfigHelper oauthApplicationConfigHelper;
    private final OauthClientHelper oauthClientHelper;

    private final JwtUtil jwtUtil;

    @Transactional
    public void signUpByOIDC() {

    }

    @Transactional
    public Jwt signInByOIDC(Long id, String idToken, ProviderType provider, String nonce) {
        OauthClient oauthClient = oauthClientHelper.getOauthClient(provider);
        OIDCPublicKeyResponse oidcPublicKeyResponse = oauthClient.getOIDCPublicKey();
        OauthApplicationConfig oauthApplicationConfig = oauthApplicationConfigHelper.getOauthApplicationConfig(provider);

        OIDCDecodePayload payload = oauthOIDCHelper.getPayloadFromIdToken(
                idToken, oauthApplicationConfig.getAuthorizationUri(),
                oauthApplicationConfig.getClientId(), nonce, oidcPublicKeyResponse);

        isValidRequestId(id, Long.parseLong(payload.sub()));

        if (oauthSearchService.isExistMember(id, provider)) {
            Member member = oauthSearchService.findMemberByOauthIdAndProvider(id, provider);
            return generateToken(JwtUserInfo.from(member));
        } else {
            // 1. redis에 {id_token, id} 저장
            // 2. null 반환 (회원가입 진행)
            return null;
        }
    }

    @Transactional
    public void signInByCode() {

    }

    @Transactional
    public void signUpByCode() {

    }

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
