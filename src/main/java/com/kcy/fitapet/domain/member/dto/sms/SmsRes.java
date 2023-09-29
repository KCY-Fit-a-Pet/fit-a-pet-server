package com.kcy.fitapet.domain.member.dto.sms;

import lombok.Builder;

import java.time.LocalDateTime;

/**
 * SMS 인증번호 발송 응답 객체
 * @param to String : 수신자 번호
 * @param sendTime LocalDateTime : 발송 시간
 * @param expireTime LocalDateTime : 만료 시간 (default: 3분)
 */
@Builder
public record SmsRes(
        String to,
        LocalDateTime sendTime,
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
