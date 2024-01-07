package com.kcy.fitapet.domain.care.exception;

import com.kcy.fitapet.global.common.response.code.StatusCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum CareErrorCode implements StatusCode {
    CATEGORY_STATUS_INVALID(HttpStatus.BAD_REQUEST, "카테고리를 생성할 수 없는 상태입니다.");

    private final HttpStatus httpStatus;
    private final String message;

    @Override
    public String getName() {
        return name();
    }
}
