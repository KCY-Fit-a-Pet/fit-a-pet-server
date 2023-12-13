package com.kcy.fitapet.global.common.util.sms.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

/**
 * Client에서 SMS 인증 요청 시 사용되는 객체
 * @param to String : 수신번호
 */
@Builder
@Schema(description = "SMS 인증 요청")
public record SmsReq(
        @Schema(description = "수신번호")
        @NotNull(message = "수신번호는 필수 입력값입니다.")
        String to,
        @Schema(description = "아이디")
        String uid
) {
}
