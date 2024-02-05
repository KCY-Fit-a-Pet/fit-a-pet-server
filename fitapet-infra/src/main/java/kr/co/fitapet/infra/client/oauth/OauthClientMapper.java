package kr.co.fitapet.infra.client.oauth;

import kr.co.fitapet.common.execption.GlobalErrorException;
import kr.co.fitapet.infra.client.oauth.type.ProviderType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OauthClientMapper {
    private final OauthClient kakaoOauthClient;
    private final OauthClient googleOauthClient;
    private final OauthClient appleOauthClient;

    public OauthClient getOauthClient(ProviderType provider) {
        return switch (provider) {
            case KAKAO -> kakaoOauthClient;
            case GOOGLE -> googleOauthClient;
            case APPLE -> appleOauthClient;
            default -> throw new IllegalArgumentException("Invalid provider type");
        };
    }
}
