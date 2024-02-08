package kr.co.fitapet.api.common.security.jwt.dto;

import lombok.Builder;

@Builder
public record Jwt(
        String accessToken,
        String refreshToken
) {
    public static Jwt of(String accessToken, String refreshToken) {
        return Jwt.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }
}
