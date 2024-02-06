package kr.co.fitapet.domain.domains.care_log.dto;


import kr.co.fitapet.domain.common.annotation.Dto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Dto(name = "careLog")
public record CareLogInfo(
        String logDate,
        String uid
) {
    public static CareLogInfo of(LocalDateTime logDate, String uid) {
        return new CareLogInfo(
                logDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                uid
        );
    }
}
