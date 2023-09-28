package com.kcy.fitapet.global.common.response;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * API Response의 success, fail에 대한 공통적인 응답을 정의한다.
 * @param <T>
 */
@BasicResponse
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SuccessResponse<T> {
    private final String status = "success";
    private T data;

    @Builder
    private SuccessResponse(T data) {
        this.data = data;
    }

    /**
     * 전송할 데이터가 존재하는 경우
     * @param data : 전송할 데이터
     */
    public static <T> SuccessResponse<T> from(T data) {
        return SuccessResponse.<T>builder()
                .data(data)
                .build();
    }

    /**
     * 전송할 데이터가 존재하지 않는 경우
     */
    public static <T> SuccessResponse<T> empty() {
        return SuccessResponse.<T>builder()
                .data(null)
                .build();
    }
}
