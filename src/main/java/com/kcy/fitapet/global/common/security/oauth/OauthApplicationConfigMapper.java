package com.kcy.fitapet.global.common.security.oauth;

import com.kcy.fitapet.domain.oauth.exception.OauthException;
import com.kcy.fitapet.domain.oauth.type.ProviderType;
import com.kcy.fitapet.global.common.response.exception.GlobalErrorException;
import com.kcy.fitapet.global.common.security.oauth.OauthApplicationConfig;
import com.kcy.fitapet.global.common.security.oauth.apple.AppleApplicationConfig;
import com.kcy.fitapet.global.common.security.oauth.google.GoogleApplicationConfig;
import com.kcy.fitapet.global.common.security.oauth.kakao.KakaoApplicationConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OauthApplicationConfigMapper {
    private final KakaoApplicationConfig kakaoApplicationConfig;
    private final GoogleApplicationConfig googleApplicationConfig;
    private final AppleApplicationConfig appleApplicationConfig;

    public OauthApplicationConfig getOauthApplicationConfig(ProviderType provider) {
        return switch (provider) {
            case KAKAO -> kakaoApplicationConfig;
            case GOOGLE -> googleApplicationConfig;
            case APPLE -> appleApplicationConfig;
            default -> throw new GlobalErrorException(OauthException.INVALID_PROVIDER);
        };
    }
}
