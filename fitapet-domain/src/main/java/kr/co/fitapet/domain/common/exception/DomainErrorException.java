package kr.co.fitapet.domain.common.exception;

import kr.co.fitapet.common.execption.GlobalErrorException;

public class DomainErrorException extends GlobalErrorException {
    private final DomainErrorCode errorCode;

    public DomainErrorException(DomainErrorCode errorCode) {
        super(errorCode);
        this.errorCode = errorCode;
    }

    public String getExplainError() {
        return errorCode.getExplainError();
    }
}
