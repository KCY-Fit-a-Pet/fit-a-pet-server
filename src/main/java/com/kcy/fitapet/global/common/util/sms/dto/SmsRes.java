package com.kcy.fitapet.global.common.util.sms.dto;

import com.kcy.fitapet.global.common.util.bind.Dto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.time.LocalDateTime;

/**
 * SMS 인증번호 발송 응답 객체
 * @param to String : 수신자 번호
 * @param sendTime LocalDateTime : 발송 시간
 * @param expireTime LocalDateTime : 만료 시간 (default: 3분)
 */
@Builder
@Schema(description = "SMS 인증번호 발송 응답")
@Dto(name = "sms")
public record SmsRes(
        @Schema(description = "수신자 번호")
        String to,
        @Schema(description = "발송 시간")
        LocalDateTime sendTime,
        @Schema(description = "만료 시간 (default: 3분)", example = "2021-08-01T00:00:00")
        LocalDateTime expireTime
) {
    public static SmsRes of(String to, LocalDateTime sendTime, LocalDateTime expireTime) {
        return SmsRes.builder()
                .to(to)
                .sendTime(sendTime)
                .expireTime(expireTime)
                .build();
    }
}
