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
        return "GlobalErrorException(code=" + errorCode.getName()
                + ", message=" + errorCode.getMessage() + ")";
    }
}
