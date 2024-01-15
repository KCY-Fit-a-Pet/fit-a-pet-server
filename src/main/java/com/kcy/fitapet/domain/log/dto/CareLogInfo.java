package com.kcy.fitapet.domain.log.dto;

import com.kcy.fitapet.domain.log.domain.CareLog;
import com.kcy.fitapet.domain.member.domain.Member;
import com.kcy.fitapet.global.common.util.bind.Dto;

import java.time.LocalDateTime;

@Dto(name = "careLog")
public record CareLogInfo(
        LocalDateTime logDate,
        String uid
) {
    public static CareLogInfo of(LocalDateTime logDate, String uid) {
        return new CareLogInfo(
                logDate,
                uid
        );
    }
}
