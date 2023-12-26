package com.kcy.fitapet.global.common.security.oauth;

import com.kcy.fitapet.domain.oauth.exception.OauthException;
import com.kcy.fitapet.domain.oauth.type.ProviderType;
import com.kcy.fitapet.global.common.response.exception.GlobalErrorException;
import com.kcy.fitapet.global.common.security.oauth.OauthClient;
import com.kcy.fitapet.global.common.security.oauth.kakao.KakaoOauthClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OauthClientMapper {
    private final KakaoOauthClient kakaoOauthClient;

    public OauthClient getOauthClient(ProviderType provider) {
        return switch (provider) {
            case KAKAO -> kakaoOauthClient;
            case GOOGLE -> null;
            case APPLE -> null;
            default -> throw new GlobalErrorException(OauthException.INVALID_PROVIDER);
        };
    }
}
