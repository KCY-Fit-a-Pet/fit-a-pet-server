package kr.co.fitapet.api.common.security.jwt.dto;

import kr.co.fitapet.domain.domains.member.domain.Member;
import kr.co.fitapet.domain.domains.member.type.RoleType;
import lombok.Builder;

import java.math.BigInteger;

@Builder
public record JwtUserInfo  (
        Long id,
        RoleType role
) implements JwtSubInfo {
    public static JwtUserInfo of(Long id, RoleType role) {
        return new JwtUserInfo(id, role);
    }

    public static JwtUserInfo from(Member member) {
        return new JwtUserInfo(member.getId(), member.getRole());
    }

    @Override
    public String oauthId() {
        throw new UnsupportedOperationException();
    }
    @Override
    public String phoneNumber() {
        throw new UnsupportedOperationException();
    }
}