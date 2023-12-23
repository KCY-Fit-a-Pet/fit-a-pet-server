package com.kcy.fitapet.global.common.security.jwt.dto;

import lombok.Builder;

@Builder
public record Jwt(
        String accessToken,
        String refreshToken
) {
}
