package kr.co.fitapet.api.common.security.jwt.dto;

import kr.co.fitapet.domain.domains.member.type.RoleType;
import lombok.Builder;

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
