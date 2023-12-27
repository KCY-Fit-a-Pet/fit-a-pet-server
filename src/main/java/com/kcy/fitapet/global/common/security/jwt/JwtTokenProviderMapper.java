package com.kcy.fitapet.global.common.security.jwt;

import com.kcy.fitapet.global.common.security.jwt.qualifier.AccessTokenQualifier;
import com.kcy.fitapet.global.common.security.jwt.qualifier.RefreshTokenQualifier;
import com.kcy.fitapet.global.common.security.jwt.qualifier.SmsAuthTokenQualifier;
import com.kcy.fitapet.global.common.security.jwt.qualifier.SmsOauthTokenQualifier;
import org.springframework.stereotype.Component;

@Component
public class JwtTokenProviderMapper {
    private final JwtProvider accessTokenProvider;
    private final JwtProvider refreshTokenProvider;
    private final JwtProvider smsAuthTokenProvider;
    private final JwtProvider smsOauthTokenProvider;

    public JwtTokenProviderMapper(
            @AccessTokenQualifier JwtProvider accessTokenProvider,
            @RefreshTokenQualifier JwtProvider refreshTokenProvider,
            @SmsAuthTokenQualifier JwtProvider smsAuthTokenProvider,
            @SmsOauthTokenQualifier JwtProvider smsOauthTokenProvider) {
        this.accessTokenProvider = accessTokenProvider;
        this.refreshTokenProvider = refreshTokenProvider;
        this.smsAuthTokenProvider = smsAuthTokenProvider;
        this.smsOauthTokenProvider = smsOauthTokenProvider;
    }

    public JwtProvider getProvider(JwtTokenType type) {
        return switch (type) {
            case ACCESS_TOKEN -> accessTokenProvider;
            case REFRESH_TOKEN -> refreshTokenProvider;
            case SMS_AUTH_TOKEN -> smsAuthTokenProvider;
            case SMS_OAUTH_TOKEN -> smsOauthTokenProvider;
        };
    }
}
