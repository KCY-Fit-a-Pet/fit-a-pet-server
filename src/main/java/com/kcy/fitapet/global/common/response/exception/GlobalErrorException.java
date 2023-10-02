package com.kcy.fitapet.global.common.response.exception;

import com.kcy.fitapet.global.common.response.code.ErrorCode;
import lombok.Getter;

@Getter
public class GlobalErrorException extends RuntimeException {
    private final ErrorCode errorCode;

    public GlobalErrorException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    @Override
    public String toString() {
        return String.format("GlobalErrorException(code=%s, message=%s)",
                errorCode.name(), errorCode.getMessage());
    }
}
