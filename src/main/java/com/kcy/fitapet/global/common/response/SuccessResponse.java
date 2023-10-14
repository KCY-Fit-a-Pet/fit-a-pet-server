package com.kcy.fitapet.global.common.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * API Response의 success에 대한 공통적인 응답을 정의한다.
 * @param <T>
 */
@BasicResponse
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Schema(description = "API 응답 - 성공")
public class SuccessResponse<T> {
    @Schema(description = "응답 상태", defaultValue = "success")
    private final String status = "success";
    @Schema(description = "응답 코드", example = "200")
    private T data;

    @Builder
    private SuccessResponse(T data) {
        this.data = data;
    }

    /**
     * 전송할 Application Level Data를 설정한다.
     * @param data : 전송할 데이터
     */
    public static <T> SuccessResponse<T> from(T data) {
        return SuccessResponse.<T>builder()
                .data(data)
                .build();
    }

    /**
     * 전송할 Application Level Data가 없는 경우 사용한다.
     */
    public static SuccessResponse<?> noContent() {
        return SuccessResponse.builder().build();
    }
}