package com.kcy.fitapet.api.auth.dto.sms;

import lombok.Builder;

import java.time.LocalDateTime;

/**
 * Client에서 받은 SMS API 요청에 대한 응답 객체
 * @param requestId String : 요청 ID
 * @param requestTime LocalDateTime : 요청 시간
 * @param statusCode String : 응답 코드
 * @param statusName String : 응답 상태
 */
@Builder
public record SensRes(
        String requestId,
        LocalDateTime requestTime,
        String statusCode,
        String statusName
) {


    public static SensRes of(String requestId, LocalDateTime requestTime, String statusCode, String statusName) {
        return SensRes.builder()
                .requestId(requestId)
                .requestTime(requestTime)
                .statusCode(statusCode)
                .statusName(statusName)
                .build();
    }
}
