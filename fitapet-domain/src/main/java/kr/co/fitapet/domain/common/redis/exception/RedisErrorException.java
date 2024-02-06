package kr.co.fitapet.domain.common.redis.exception;

import kr.co.fitapet.common.execption.BaseErrorCode;
import kr.co.fitapet.common.execption.CausedBy;
import kr.co.fitapet.common.execption.GlobalErrorException;

public class RedisErrorException extends GlobalErrorException {
    private final RedisErrorCode errorCode;

    public RedisErrorException(RedisErrorCode baseErrorCode) {
        super(baseErrorCode);
        this.errorCode = baseErrorCode;
    }

    public CausedBy causedBy() {
        return errorCode.causedBy();
    }

    public RedisErrorCode getErrorCode() {
        return errorCode;
    }
}
