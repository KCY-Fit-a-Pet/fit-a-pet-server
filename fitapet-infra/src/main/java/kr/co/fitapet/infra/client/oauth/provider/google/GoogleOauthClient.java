package kr.co.fitapet.infra.client.oauth.provider.google;

import kr.co.fitapet.infra.client.oauth.OauthClient;
import kr.co.fitapet.infra.client.oauth.dto.OIDCPublicKeyResponse;
import kr.co.fitapet.infra.config.feign.GoogleOauthConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(
        name = "GoogleOauthClient",
        url = "${oauth2.client.provider.google.jwks-uri}",
        configuration = GoogleOauthConfig.class,
        qualifiers = "googleOauthClient",
        primary = false
)
public interface GoogleOauthClient extends OauthClient {
    @Override
    @Cacheable(value = "GoogleOauth", cacheManager = "oidcCacheManger")
    @GetMapping("/oauth2/v3/certs")
    OIDCPublicKeyResponse getOIDCPublicKey();
}
