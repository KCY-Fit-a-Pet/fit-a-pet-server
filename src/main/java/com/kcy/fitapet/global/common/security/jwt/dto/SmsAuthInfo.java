package com.kcy.fitapet.global.common.security.jwt.dto;

import com.kcy.fitapet.global.common.security.jwt.entity.JwtDto;
import lombok.Builder;

@Builder
public record SmsAuthInfo(
        Long userId,
        String phoneNumber
) implements JwtDto {
    public static SmsAuthInfo of(Long userId, String phoneNumber) {
        return new SmsAuthInfo(userId, phoneNumber);
    }
}
