package kr.co.fitapet.api.common.security.jwt;

import com.kcy.fitapet.global.common.security.jwt.qualifier.AccessTokenQualifier;
import com.kcy.fitapet.global.common.security.jwt.qualifier.RefreshTokenQualifier;
import com.kcy.fitapet.global.common.security.jwt.qualifier.SmsAuthTokenQualifier;
import com.kcy.fitapet.global.common.security.jwt.qualifier.SmsOauthTokenQualifier;
import org.springframework.stereotype.Component;

@Component
public class JwtProviderMapper {
    private final JwtProvider accessTokenProvider;
    private final JwtProvider refreshTokenProvider;
    private final JwtProvider smsAuthTokenProvider;
    private final JwtProvider smsOauthTokenProvider;

    public JwtProviderMapper(
            @AccessTokenQualifier JwtProvider accessTokenProvider,
            @RefreshTokenQualifier JwtProvider refreshTokenProvider,
            @SmsAuthTokenQualifier JwtProvider smsAuthTokenProvider,
            @SmsOauthTokenQualifier JwtProvider smsOauthTokenProvider) {
        this.accessTokenProvider = accessTokenProvider;
        this.refreshTokenProvider = refreshTokenProvider;
        this.smsAuthTokenProvider = smsAuthTokenProvider;
        this.smsOauthTokenProvider = smsOauthTokenProvider;
    }

    public JwtProvider getProvider(AuthConstants type) {
        return switch (type) {
            case ACCESS_TOKEN -> accessTokenProvider;
            case REFRESH_TOKEN -> refreshTokenProvider;
            case SMS_AUTH_TOKEN -> smsAuthTokenProvider;
            case SMS_OAUTH_TOKEN -> smsOauthTokenProvider;
            default -> throw new IllegalArgumentException("Unexpected value: " + type);
        };
    }
}
