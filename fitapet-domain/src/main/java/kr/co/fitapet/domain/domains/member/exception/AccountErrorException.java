package kr.co.fitapet.domain.domains.member.exception;

import kr.co.fitapet.common.execption.CausedBy;
import kr.co.fitapet.common.execption.GlobalErrorException;

public class AccountErrorException extends GlobalErrorException {
    private final AccountErrorCode errorCode;

    public AccountErrorException(AccountErrorCode errorCode) {
        super(errorCode);
        this.errorCode = errorCode;
    }

    public CausedBy causedBy() {
        return errorCode.causedBy();
    }
}
