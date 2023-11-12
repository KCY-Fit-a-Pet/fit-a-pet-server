package com.kcy.fitapet.global.common.response.exception;

import com.kcy.fitapet.global.common.response.code.ErrorCode;
import com.kcy.fitapet.global.common.response.code.StatusCode;
import lombok.Getter;

@Getter
public class GlobalErrorException extends RuntimeException {
    private final StatusCode errorCode;

    public GlobalErrorException(StatusCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    @Override
    public String toString() {
        return String.format("GlobalErrorException(code=%s, message=%s)",
                errorCode.getName(), errorCode.getMessage());
    }
}
