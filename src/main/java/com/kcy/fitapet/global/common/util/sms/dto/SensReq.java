package com.kcy.fitapet.global.common.util.sms.dto;

import lombok.Builder;

import java.util.List;

@Builder
public record SensReq(
        String type,
        String contentType,
        String countryCode,
        String from,
        String content,
        List<SmsReq> messages
) {
    /**
     * NCP SMS API 요청 객체 생성
     * @param type String : SMS | LMS | MMS
     * @param contentType String : COMM | AD
     * @param countryCode String : 국가번호
     * @param from String : 발신번호
     * @param content String : 메시지 내용
     * @param messages List<SmsReq> : 메시지 정보 (to: 수신번호, subject: 개별 메시지 제목, content: 개별 메시지 내용)
     * @return SensReq : NCP SMS API 요청 객체
     */
    public static SensReq of(String type, String contentType, String countryCode, String from, String content, List<SmsReq> messages) {
        return SensReq.builder()
                .type(type)
                .contentType(contentType)
                .countryCode(countryCode)
                .from(from)
                .content(content)
                .messages(messages)
                .build();
    }
}
