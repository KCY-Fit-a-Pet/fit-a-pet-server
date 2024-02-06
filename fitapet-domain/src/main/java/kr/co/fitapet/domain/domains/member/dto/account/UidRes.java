package kr.co.fitapet.domain.domains.member.dto.account;


import kr.co.fitapet.domain.common.annotation.Dto;

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
