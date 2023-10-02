package com.kcy.fitapet.global.common.util.jwt;

import lombok.Getter;

@Getter
public enum AuthConstants {
    AUTH_HEADER("Authorization"), TOKEN_TYPE("Bearer "),
    ACCESS_TOKEN("accessToken"), REFRESH_TOKEN("refreshToken");

    private final String value;

    AuthConstants(String value) {
        this.value = value;
    }

    @Override public String toString() {
        return String.format("AuthConstants(value=%s)", this.value);
    }
}