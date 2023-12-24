package com.kcy.fitapet.global.common.util.sms.dto;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record SensInfo(
        String requestId,
        String code,
        LocalDateTime requestTime,
        String statusCode,
        String statusName
) {
    public static SensInfo from(SensRes sensRes, String code) {
        return SensInfo.builder()
                .requestId(sensRes.requestId())
                .code(code)
                .requestTime(sensRes.requestTime())
                .statusCode(sensRes.statusCode())
                .statusName(sensRes.statusName())
                .build();
    }
}
