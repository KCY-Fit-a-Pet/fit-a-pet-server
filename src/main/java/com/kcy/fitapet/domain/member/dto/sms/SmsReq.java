package com.kcy.fitapet.domain.member.dto.sms;

import lombok.Builder;

/**
 * Client에서 SMS 인증 요청 시 사용되는 객체
 * @param to String : 수신번호
 */
@Builder
public record SmsReq(
        String to
) {
}
