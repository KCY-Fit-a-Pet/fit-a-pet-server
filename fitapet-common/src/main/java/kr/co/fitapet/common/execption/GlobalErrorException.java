package kr.co.fitapet.common.execption;

import lombok.Getter;

@Getter
public class GlobalErrorException extends RuntimeException {
    private final BaseErrorCode baseErrorCode;

    public GlobalErrorException(BaseErrorCode baseErrorCode) {
        super(baseErrorCode.causedBy().message());
        this.baseErrorCode = baseErrorCode;
    }

    public CausedBy causedBy() {
        return baseErrorCode.causedBy();
    }

    @Override
    public String toString() {
        return "GlobalErrorException(code=" + baseErrorCode.causedBy().name()
                + ", message=" + baseErrorCode.getExplainError() + ")";
    }
}
