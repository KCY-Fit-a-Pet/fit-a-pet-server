package kr.co.fitapet.api.common.security.jwt.consts;

import lombok.Getter;

@Getter
public enum JwtType {
    ACCESS_TOKEN("accessToken"), REFRESH_TOKEN("refreshToken"),
    SMS_AUTH_TOKEN("smsAuthToken"), SMS_OAUTH_TOKEN("smsOauthToken");

    private final String value;

    JwtType(String value) {
        this.value = value;
    }
}
