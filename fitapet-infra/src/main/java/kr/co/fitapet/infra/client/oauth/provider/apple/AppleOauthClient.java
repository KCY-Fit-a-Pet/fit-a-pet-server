package kr.co.fitapet.infra.client.oauth.provider.apple;

import kr.co.fitapet.infra.client.oauth.OauthClient;
import kr.co.fitapet.infra.client.oauth.dto.OIDCPublicKeyResponse;
import kr.co.fitapet.infra.config.feign.AppleOauthConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(
        name = "AppleOauthClient",
        url = "${oauth2.client.provider.apple.jwks-uri}",
        configuration = AppleOauthConfig.class,
        qualifiers = "AppleOauth"
)
public interface AppleOauthClient extends OauthClient {
    @Override
    @Cacheable(value = "AppleOauth", cacheManager = "oidcCacheManger")
    @GetMapping("/auth/keys")
    OIDCPublicKeyResponse getOIDCPublicKey();
}
