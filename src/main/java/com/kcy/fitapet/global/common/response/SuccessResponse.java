package com.kcy.fitapet.global.common.response;

import com.kcy.fitapet.global.common.util.bind.Dto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Map;

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
    @Schema(description = "응답 코드", example = "data or no_content")
    private Map<String, T> data;

    @Builder
    private SuccessResponse(Map<String, T> data) {
        this.data = data;
    }

    public static <T> SuccessResponse<T> from(String key, T data) {
        return SuccessResponse.<T>builder()
                .data(Map.of(key, data))
                .build();
    }

    /**
     * 전송할 Application Level Data를 설정한다.
     * @param data : 전송할 데이터
     */
    public static <T> SuccessResponse<T> from(T data) {
        String key = getDtoName(data);

        if (data instanceof Map) {
            key = ((Map<?, ?>) data).keySet().stream().findFirst().orElse(null).toString();
            data = (T) ((Map<?, ?>) data).get(key);
        }
        if (key == null) key = "data";

        return from(key, data);
    }

    /**
     * 전송할 Application Level Data가 없는 경우 사용한다.
     */
    public static SuccessResponse<?> noContent() {
        return SuccessResponse.builder().build();
    }

    private static <T> String getDtoName(T data) {
        Class<?> clazz = data.getClass();
        if (clazz != null && clazz.isAnnotationPresent(Dto.class)) {
            return clazz.getAnnotation(Dto.class).name();
        }
        return null;
    }
}