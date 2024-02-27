package kr.co.fitapet.domain.domains.device.exception;

import kr.co.fitapet.common.execption.BaseErrorCode;
import kr.co.fitapet.common.execption.CausedBy;
import kr.co.fitapet.common.execption.GlobalErrorException;

public class DeviceTokenErrorException extends GlobalErrorException {
    private final DeviceTokenErrorCode errorCode;

    public DeviceTokenErrorException(DeviceTokenErrorCode errorCode) {
        super(errorCode);
        this.errorCode = errorCode;
    }

    public CausedBy causedBy() {
        return errorCode.causedBy();
    }

    public BaseErrorCode getErrorCode() {
        return errorCode;
    }
}
