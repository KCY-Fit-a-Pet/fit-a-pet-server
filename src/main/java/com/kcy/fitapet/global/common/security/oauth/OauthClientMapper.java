package com.kcy.fitapet.global.common.security.oauth;

import com.kcy.fitapet.domain.oauth.exception.OauthException;
import com.kcy.fitapet.domain.oauth.type.ProviderType;
import com.kcy.fitapet.global.common.response.exception.GlobalErrorException;
import com.kcy.fitapet.global.common.security.oauth.OauthClient;
import com.kcy.fitapet.global.common.security.oauth.apple.AppleOauthClient;
import com.kcy.fitapet.global.common.security.oauth.google.GoogleOauthClient;
import com.kcy.fitapet.global.common.security.oauth.kakao.KakaoOauthClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OauthClientMapper {
    private final KakaoOauthClient kakaoOauthClient;
    private final GoogleOauthClient googleOauthClient;
    private final AppleOauthClient appleOauthClient;

    public OauthClient getOauthClient(ProviderType provider) {
        return switch (provider) {
            case KAKAO -> kakaoOauthClient;
            case GOOGLE -> googleOauthClient;
            case APPLE -> appleOauthClient;
            default -> throw new GlobalErrorException(OauthException.INVALID_PROVIDER);
        };
    }
}
