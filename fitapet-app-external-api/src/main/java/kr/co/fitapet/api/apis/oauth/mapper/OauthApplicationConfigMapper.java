package kr.co.fitapet.api.apis.oauth.mapper;

import kr.co.fitapet.infra.client.oauth.OauthApplicationConfig;
import kr.co.fitapet.infra.client.oauth.provider.apple.AppleApplicationConfig;
import kr.co.fitapet.infra.client.oauth.provider.google.GoogleApplicationConfig;
import kr.co.fitapet.infra.client.oauth.provider.kakao.KakaoApplicationConfig;
import kr.co.fitapet.infra.client.oauth.type.Provider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class OauthApplicationConfigMapper {
    private final Map<Provider, OauthApplicationConfig> oauthApplicationConfigMap;

    public OauthApplicationConfigMapper(
            KakaoApplicationConfig kakaoApplicationConfig,
            GoogleApplicationConfig googleApplicationConfig,
            AppleApplicationConfig appleApplicationConfig
    ) {
        this.oauthApplicationConfigMap = Map.of(
                Provider.KAKAO, kakaoApplicationConfig,
                Provider.GOOGLE, googleApplicationConfig,
                Provider.APPLE, appleApplicationConfig
        );
    }

    public OauthApplicationConfig getOauthApplicationConfig(Provider provider) {
        return oauthApplicationConfigMap.get(provider);
    }
}
