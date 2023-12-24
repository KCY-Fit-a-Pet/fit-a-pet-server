package com.kcy.fitapet.global.common.security.oauth;

import com.kcy.fitapet.global.common.security.oauth.dto.OIDCPublicKeyResponse;
import org.springframework.cloud.openfeign.FeignClient;

public interface OauthClient {
    OIDCPublicKeyResponse getOIDCPublicKey();
}
