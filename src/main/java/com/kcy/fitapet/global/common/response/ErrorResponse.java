package com.kcy.fitapet.global.common.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@BasicResponse
@Builder
@Schema(description = "API 응답 - 에러")
public record ErrorResponse(
        @Schema(description = "응답 상태", defaultValue = "error") String status,
        @Schema(description = "에러 메시지", example = "reason") String message
) {
    public static ErrorResponse of(String message) {
        return ErrorResponse.builder()
                .status("error")
                .message(message)
                .build();
    }
}
