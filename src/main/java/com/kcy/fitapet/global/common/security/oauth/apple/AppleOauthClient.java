package com.kcy.fitapet.global.common.security.oauth.apple;

import com.kcy.fitapet.global.common.security.oauth.OauthClient;
import com.kcy.fitapet.global.common.security.oauth.dto.OIDCPublicKeyResponse;
import com.kcy.fitapet.global.config.feign.AppleOauthConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(
        name = "AppleOauthClient",
        url = "${oauth2.client.provider.apple.jwks-uri}",
        configuration = AppleOauthConfig.class
)
public interface AppleOauthClient extends OauthClient {
    @Override
    @Cacheable(value = "AppleOauth", cacheManager = "oidcCacheManger")
    @GetMapping("/auth/keys")
    OIDCPublicKeyResponse getOIDCPublicKey();
}
