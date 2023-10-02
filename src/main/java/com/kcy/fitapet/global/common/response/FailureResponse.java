package com.kcy.fitapet.global.common.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@BasicResponse
@Getter
@Schema(description = "API 응답 - 실패")
public class FailureResponse {
    @Schema(description = "응답 상태", defaultValue = "fail")
    private final String status = "fail";
    @Schema(description = "응답 데이터", example = "{\"field\":\"reason\"}")
    private final Object data;

    @Builder
    private FailureResponse(final Object data) {
        this.data = data;
    }

    /**
     * 단일 필드 에러를 응답으로 변환한다.
     * @param field : 에러가 발생한 필드
     * @param reason : 에러 이유
     * @return FailureResponse
     */
    public static FailureResponse of(String field, String reason) {
        return FailureResponse.builder()
                .data(Map.of(field, reason))
                .build();
    }

    /**
     * BindingResult를 통해 발생한 에러를 응답으로 변환한다.
     * @param bindingResult : BindingResult
     * @return FailureResponse
     */
    public static FailureResponse from(final BindingResult bindingResult) {
        Map<String, String> fieldErrors = new HashMap<>();
        for (FieldError fieldError : bindingResult.getFieldErrors()) {
            fieldErrors.put(fieldError.getField(), fieldError.getDefaultMessage());
        }

        if (fieldErrors.size() == 1) {
            Map.Entry<String, String> entry = fieldErrors.entrySet().iterator().next();
            return of(entry.getKey(), entry.getValue());
        } else if (fieldErrors.size() > 1) {
            return multiFieldError(fieldErrors);
        }
        return null;
    }

    private static <T extends Map<String, String>> FailureResponse multiFieldError(final T fieldErrors) {
        List<Map<String, String>> fieldErrorsList = new ArrayList<>();
        for (Map.Entry<String, String> entry : fieldErrors.entrySet()) {
            fieldErrorsList.add(Map.of(entry.getKey(), entry.getValue()));
        }
        return FailureResponse.builder()
                .data(fieldErrorsList)
                .build();
    }

}
