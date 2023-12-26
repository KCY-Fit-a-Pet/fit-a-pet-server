package com.kcy.fitapet.global.common.security.oauth;

import com.kcy.fitapet.domain.oauth.exception.OauthException;
import com.kcy.fitapet.domain.oauth.type.ProviderType;
import com.kcy.fitapet.global.common.response.exception.GlobalErrorException;
import com.kcy.fitapet.global.common.security.oauth.OauthApplicationConfig;
import com.kcy.fitapet.global.common.security.oauth.kakao.KakaoApplicationConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OauthApplicationConfigMapper {
    private final KakaoApplicationConfig kakaoApplicationConfig;

    public OauthApplicationConfig getOauthApplicationConfig(ProviderType provider) {
        return switch (provider) {
            case KAKAO -> kakaoApplicationConfig;
            case GOOGLE -> null;
            case APPLE -> null;
            case NAVER -> null;
            default -> throw new GlobalErrorException(OauthException.INVALID_PROVIDER);
        };
    }
}
