package com.kcy.fitapet.domain.member.dto.account;

import com.kcy.fitapet.global.common.util.bind.Dto;

import java.time.LocalDateTime;

@Dto(name = "member")
public record UidRes(
       String uid,
       LocalDateTime createdAt
) {
    public static UidRes of(String uid, LocalDateTime createdAt) {
        return new UidRes(uid, createdAt);
    }
}
