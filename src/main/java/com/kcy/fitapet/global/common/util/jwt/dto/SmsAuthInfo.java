package com.kcy.fitapet.global.common.util.jwt.dto;

import com.kcy.fitapet.global.common.util.jwt.entity.JwtDto;
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
