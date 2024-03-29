package kr.co.fitapet.api.apis.oauth.helper;

import kr.co.fitapet.infra.client.oauth.OauthOIDCProvider;
import kr.co.fitapet.infra.client.oauth.dto.OIDCDecodePayload;
import kr.co.fitapet.infra.client.oauth.dto.OIDCPublicKey;
import kr.co.fitapet.infra.client.oauth.dto.OIDCPublicKeyResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class OauthOIDCHelper {
    private final OauthOIDCProvider oauthOIDCProvider;

    /**
     * ID Token의 payload를 추출하는 메서드 <br/>
     * OAuth 2.0 spec에 따라 ID Token의 유효성 검사 수행 <br/>
     * @param token : idToken
     * @param iss : ID Token을 발급한 provider의 URL
     * @param aud : ID Token이 발급된 앱의 앱 키
     * @param nonce : 인증 서버 로그인 요청 시 전달한 임의의 문자열
     * @param response : 공개키 목록
     * @return OIDCDecodePayload : ID Token의 payload
     */
    public OIDCDecodePayload getPayloadFromIdToken(String token, String iss, String aud, String nonce, OIDCPublicKeyResponse response) {
        String kid = getKidFromUnsignedIdToken(token, iss, aud, nonce);

        OIDCPublicKey key = response.getKeys().stream()
                .filter(k -> k.kid().equals(kid))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No matching key found"));
        return oauthOIDCProvider.getOIDCTokenBody(token, key.n(), key.e());
    }

    private String getKidFromUnsignedIdToken(String token, String iss, String aud, String nonce) {
        return oauthOIDCProvider.getKidFromUnsignedTokenHeader(token, iss, aud, nonce);
    }
}
