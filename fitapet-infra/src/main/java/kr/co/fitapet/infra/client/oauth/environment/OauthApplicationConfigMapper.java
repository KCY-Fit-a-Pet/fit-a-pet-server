package kr.co.fitapet.infra.client.oauth.environment;

import kr.co.fitapet.infra.client.oauth.type.Provider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OauthApplicationConfigMapper {
    private final OauthApplicationConfig kakaoApplicationConfig;
    private final OauthApplicationConfig googleApplicationConfig;
    private final OauthApplicationConfig appleApplicationConfig;

    public OauthApplicationConfig getOauthApplicationConfig(Provider provider) {
        return switch (provider) {
            case KAKAO -> kakaoApplicationConfig;
            case GOOGLE -> googleApplicationConfig;
            case APPLE -> appleApplicationConfig;
            default -> throw new IllegalArgumentException("Invalid provider type");
        };
    }
}