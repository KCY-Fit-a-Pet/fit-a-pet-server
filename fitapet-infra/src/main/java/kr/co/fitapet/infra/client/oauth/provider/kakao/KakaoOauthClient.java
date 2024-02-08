package kr.co.fitapet.infra.client.oauth.provider.kakao;

import kr.co.fitapet.infra.client.oauth.OauthClient;
import kr.co.fitapet.infra.client.oauth.dto.OIDCPublicKeyResponse;
import kr.co.fitapet.infra.config.feign.KakaoOauthConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(
        name = "KakaoOauthClient",
        url = "${oauth2.client.provider.kakao.jwks-uri}",
        configuration = KakaoOauthConfig.class,
        qualifiers = "kakaoOauthClient"
)
public interface KakaoOauthClient extends OauthClient {
    @Override
    @Cacheable(value = "KakaoOauth", cacheManager = "oidcCacheManger")
    @GetMapping("/.well-known/jwks.json")
    OIDCPublicKeyResponse getOIDCPublicKey();
}
