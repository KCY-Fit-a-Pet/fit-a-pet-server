package kr.co.fitapet.infra.common.execption;

import kr.co.fitapet.common.execption.CausedBy;
import kr.co.fitapet.common.execption.GlobalErrorException;
import lombok.Getter;

@Getter
public class JwtErrorException extends GlobalErrorException {
    private final JwtErrorCode errorCode;

    public JwtErrorException(JwtErrorCode baseErrorCode) {
        super(baseErrorCode);
        this.errorCode = baseErrorCode;
    }

    public CausedBy causedBy() {
        return errorCode.causedBy();
    }

    public JwtErrorCode getErrorCode() {
        return errorCode;
    }
}
