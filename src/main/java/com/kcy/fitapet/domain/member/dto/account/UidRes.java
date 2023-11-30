package com.kcy.fitapet.domain.member.dto.account;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public record UidRes(
       String uid,
       LocalDateTime createdAt
) {
    public static UidRes of(String uid, LocalDateTime createdAt) {
        return new UidRes(uid, createdAt);
    }
}
