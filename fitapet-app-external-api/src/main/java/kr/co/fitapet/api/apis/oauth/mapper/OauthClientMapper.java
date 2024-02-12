package kr.co.fitapet.api.apis.oauth.mapper;

import kr.co.fitapet.infra.client.oauth.OauthApplicationConfig;
import kr.co.fitapet.infra.client.oauth.OauthClient;
import kr.co.fitapet.infra.client.oauth.dto.OIDCPublicKeyResponse;
import kr.co.fitapet.infra.client.oauth.provider.apple.AppleOauthClient;
import kr.co.fitapet.infra.client.oauth.provider.google.GoogleOauthClient;
import kr.co.fitapet.infra.client.oauth.provider.kakao.KakaoOauthClient;
import kr.co.fitapet.infra.client.oauth.type.Provider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class OauthClientMapper {
    private final Map<Provider, OauthClient> oauthClientMap;
    private final OauthApplicationConfigMapper oauthApplicationConfigMapper;

    public OauthClientMapper(
            KakaoOauthClient kakaoOauthClient,
            GoogleOauthClient googleOauthClient,
            AppleOauthClient appleOauthClient,
            OauthApplicationConfigMapper oauthApplicationConfigMapper
    ) {
        this.oauthClientMap = Map.of(
                Provider.KAKAO, kakaoOauthClient,
                Provider.GOOGLE, googleOauthClient,
                Provider.APPLE, appleOauthClient
        );
        this.oauthApplicationConfigMapper = oauthApplicationConfigMapper;
    }

    public OIDCPublicKeyResponse getPublicKeyResponse(Provider provider) {
        return oauthClientMap.get(provider).getOIDCPublicKey();
    }

    public OauthApplicationConfig getOauthApplicationConfig(Provider provider) {
        return oauthApplicationConfigMapper.getOauthApplicationConfig(provider);
    }
}
