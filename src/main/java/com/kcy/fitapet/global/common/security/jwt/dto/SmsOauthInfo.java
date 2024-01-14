package com.kcy.fitapet.global.common.security.jwt.dto;

import com.kcy.fitapet.domain.member.type.RoleType;
import lombok.Builder;

import java.math.BigInteger;

@Builder
public record SmsOauthInfo(
        String oauthId,
        String phoneNumber
) implements JwtSubInfo {
    public static SmsOauthInfo of(String id, String phone) {
        return new SmsOauthInfo(id, phone);
    }

    @Override
    public Long id() {
        throw new UnsupportedOperationException();
    }

    @Override
    public RoleType role() {
        throw new UnsupportedOperationException();
    }
}
