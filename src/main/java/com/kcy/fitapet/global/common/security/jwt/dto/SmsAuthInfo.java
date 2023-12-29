package com.kcy.fitapet.global.common.security.jwt.dto;

import com.kcy.fitapet.domain.member.type.RoleType;
import lombok.Builder;

@Builder
public record SmsAuthInfo(
        Long id,
        String phoneNumber
) implements JwtSubInfo {
    public static SmsAuthInfo of(Long id, String phoneNumber) {
        return new SmsAuthInfo(id, phoneNumber);
    }

    @Override
    public RoleType role() {
        throw new UnsupportedOperationException();
    }
}
