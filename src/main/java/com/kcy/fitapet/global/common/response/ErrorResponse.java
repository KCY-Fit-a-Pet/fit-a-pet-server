package com.kcy.fitapet.global.common.response;

import lombok.Builder;

@BasicResponse
@Builder
public record ErrorResponse(
        String status,
        String message
) {
    public static ErrorResponse of(String message) {
        return ErrorResponse.builder()
                .status("error")
                .message(message)
                .build();
    }
}
