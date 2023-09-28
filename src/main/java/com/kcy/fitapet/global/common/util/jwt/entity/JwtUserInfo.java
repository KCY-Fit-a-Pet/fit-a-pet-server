package com.kcy.fitapet.global.common.util.jwt.entity;

import com.kcy.fitapet.domain.member.domain.Member;
import com.kcy.fitapet.domain.member.domain.RoleType;
import lombok.Builder;
import lombok.ToString;

@Builder
public record JwtUserInfo(
        Long id,
        RoleType role
) {
    public static JwtUserInfo of(Long id, RoleType role) {
        return new JwtUserInfo(id, role);
    }

    public static JwtUserInfo from(Member member) {
        return new JwtUserInfo(member.getId(), member.getRole());
    }
}