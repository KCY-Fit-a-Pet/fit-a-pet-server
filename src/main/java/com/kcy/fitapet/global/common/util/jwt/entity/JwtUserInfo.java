package com.kcy.fitapet.global.common.util.jwt.entity;

import com.kcy.fitapet.domain.member.domain.Member;
import com.kcy.fitapet.domain.member.type.RoleType;
import lombok.Builder;

@Builder
public record JwtUserInfo  (
        Long id,
        RoleType role
) implements JwtDto {
    public static JwtUserInfo of(Long id, RoleType role) {
        return new JwtUserInfo(id, role);
    }

    public static JwtUserInfo from(Member member) {
        return new JwtUserInfo(member.getId(), member.getRole());
    }
}