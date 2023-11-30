package com.kcy.fitapet.domain.member.dto.auth;

import com.kcy.fitapet.global.common.util.bind.Dto;

import java.time.LocalDateTime;

@Dto(name = "member")
public record MemberFindRes(
        String uid,
        LocalDateTime createdAt
) {
    public static MemberFindRes of(String uid, LocalDateTime createdAt) {
        return new MemberFindRes(uid, createdAt);
    }
}
