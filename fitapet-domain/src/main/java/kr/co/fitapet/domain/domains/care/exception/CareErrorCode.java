package kr.co.fitapet.domain.domains.care.exception;

import kr.co.fitapet.common.execption.BaseErrorCode;
import kr.co.fitapet.common.execption.CausedBy;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import static kr.co.fitapet.common.execption.StatusCode.BAD_REQUEST;

@Getter
@RequiredArgsConstructor
public enum CareErrorCode implements BaseErrorCode {
    /* 400 BAD_REQUEST */
    CATEGORY_STATUS_INVALID(BAD_REQUEST.getCode(), "카테고리를 생성할 수 없는 상태입니다."),
    ALREADY_CARED(BAD_REQUEST.getCode(), "이미 케어한 날짜에 대한 요청"),
    NOT_TODAY_CARE(BAD_REQUEST.getCode(), "오늘 날짜에 대한 요청이 아님")
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
