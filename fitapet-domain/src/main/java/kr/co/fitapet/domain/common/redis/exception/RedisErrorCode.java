package kr.co.fitapet.domain.common.redis.exception;

import kr.co.fitapet.common.execption.BaseErrorCode;
import kr.co.fitapet.common.execption.CausedBy;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum RedisErrorCode implements BaseErrorCode {
    /* 400 BAD REQUEST */
    MISS_MATCHED_VALUES(400, "값이 일치하지 않습니다."),

    /* 404 NOT FOUND */
    NOT_FOUND_KEY(404, "키를 찾을 수 없습니다."),

    ;

    private final int code;
    private final String message;

    @Override
    public CausedBy causedBy() {
        return CausedBy.of(code, name(), message);
    }

    @Override
    public String getExplainError() throws NoSuchFieldError {
        return message;
    }
}
