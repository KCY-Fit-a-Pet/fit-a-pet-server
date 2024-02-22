package kr.co.fitapet.domain.domains.care_log.exception;

import kr.co.fitapet.common.execption.BaseErrorCode;
import kr.co.fitapet.common.execption.CausedBy;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import static kr.co.fitapet.common.execption.StatusCode.NOT_FOUND;

@Getter
@RequiredArgsConstructor
public enum CareLogErrorCode implements BaseErrorCode {
    /* 404 NOT_FOUND */
    NOT_FOUND_CARE_LOG(NOT_FOUND.getCode(), "케어 로그를 찾을 수 없음");

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
