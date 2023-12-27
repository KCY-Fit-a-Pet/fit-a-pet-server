package com.kcy.fitapet.global.common.security.oauth.google;

import com.kcy.fitapet.global.common.security.oauth.OauthClient;
import com.kcy.fitapet.global.common.security.oauth.dto.OIDCPublicKeyResponse;
import com.kcy.fitapet.global.config.feign.GoogleOauthConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(
        name = "GoogleOauthClient",
        url = "${oauth2.client.provider.google.jwks-uri}",
        configuration = GoogleOauthConfig.class
)
public interface GoogleOauthClient extends OauthClient {
    @Override
    @Cacheable(value = "GoogleOauth", cacheManager = "oidcCacheManger")
    @GetMapping("/oauth2/v3/certs")
    OIDCPublicKeyResponse getOIDCPublicKey();
}
