package com.kcy.fitapet.global.common.security.oauth;

import com.kcy.fitapet.global.common.security.oauth.dto.OIDCPublicKeyResponse;

public interface OauthClient {
    OIDCPublicKeyResponse getOIDCPublicKey();
}
