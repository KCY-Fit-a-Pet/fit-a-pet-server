package kr.co.fitapet.infra.client.oauth;


import kr.co.fitapet.infra.client.oauth.dto.OIDCPublicKeyResponse;

public interface OauthClient {
    OIDCPublicKeyResponse getOIDCPublicKey();
}
