package kr.co.fitapet.api.common.security.jwt.consts;

import lombok.Getter;

@Getter
public enum AuthConstants {
    ACCESS_TOKEN("accessToken"), REFRESH_TOKEN("refreshToken"),
    AUTH_HEADER("Authorization"), TOKEN_TYPE("Bearer "),
    REISSUED_ACCESS_TOKEN("X-Access-Token");

    private final String value;

    AuthConstants(String value) {
        this.value = value;
    }

    @Override public String toString() {
        return String.format("AuthConstants(value=%s)", this.value);
    }
}