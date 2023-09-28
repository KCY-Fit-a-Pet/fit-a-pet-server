package com.kcy.fitapet.global.common.response.exception;

import com.kcy.fitapet.global.common.response.code.ErrorCode;
import lombok.Getter;

@Getter
public class GlobalErrorException extends RuntimeException {
    private final ErrorCode errorCode;
    private final String causedBy;

    public GlobalErrorException(ErrorCode errorCode, String causedBy) {
        super(String.format("GlobalErrorException(code=%s, message=%s, causedBy=%s)",
                errorCode.name(), errorCode.getMessage(), causedBy));
        this.errorCode = errorCode;
        this.causedBy = causedBy;
    }

    @Override
    public String toString() {
        return String.format("GlobalErrorException(code=%s, message=%s, causedBy=%s)",
                errorCode.name(), errorCode.getMessage(), causedBy);
    }
}
