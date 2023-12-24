package com.kcy.fitapet.global.common.security.oauth.kakao;

import com.kcy.fitapet.global.common.security.oauth.OauthClient;
import com.kcy.fitapet.global.common.security.oauth.dto.OIDCPublicKeyResponse;
import com.kcy.fitapet.global.config.feign.KakaoOauthConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(
        name = "KakaoOauthClient",
        url = "${oauth2.client.provider.kakao.authorization-uri}",
        configuration = KakaoOauthConfig.class
)
public interface KakaoOauthClient extends OauthClient {
    @Override
    @Cacheable(value = "KakaoOauth", cacheManager = "oidcCacheManger")
    @GetMapping("/.well-knowm/jwks.json")
    OIDCPublicKeyResponse getOIDCPublicKey();
}
