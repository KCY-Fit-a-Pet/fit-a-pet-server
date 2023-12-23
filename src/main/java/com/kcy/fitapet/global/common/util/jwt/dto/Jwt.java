package com.kcy.fitapet.global.common.util.jwt.dto;

public record Jwt(
        String accessToken,
        String refreshToken
) {
    public static Jwt of(String accessToken, String refreshToken) {
        return new Jwt(accessToken, refreshToken);
    }
}
