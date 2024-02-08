package kr.co.fitapet.api.common.security.jwt.dto;

import kr.co.fitapet.domain.domains.member.type.RoleType;
import lombok.Builder;

import java.math.BigInteger;

@Builder
public record SmsAuthInfo(
        Long id,
        String phoneNumber
) implements JwtSubInfo {
    public static SmsAuthInfo of(Long id, String phoneNumber) {
        return new SmsAuthInfo(id, phoneNumber);
    }

    @Override
    public String oauthId() {
        throw new UnsupportedOperationException();
    }
    @Override
    public RoleType role() {
        throw new UnsupportedOperationException();
    }
}
