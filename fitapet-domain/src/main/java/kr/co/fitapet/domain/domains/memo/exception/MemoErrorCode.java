package kr.co.fitapet.domain.domains.memo.exception;

import kr.co.fitapet.common.execption.BaseErrorCode;
import kr.co.fitapet.common.execption.CausedBy;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import static kr.co.fitapet.common.execption.StatusCode.NOT_FOUND;

@Getter
@RequiredArgsConstructor
public enum MemoErrorCode implements BaseErrorCode {
    /* 400 BAD_REQUEST */
    MEMO_TITLE_NOT_EMPTY(400, "메모 제목은 공백을 허용하지 않습니다."),
    MEMO_CONTENT_NOT_EMPTY(400, "메모 내용은 공백을 허용하지 않습니다."),

    /* 404 NOT_FOUND */
    MEMO_CATEGORY_NOT_FOUND(NOT_FOUND.getCode(), "메모 카테고리를 찾을 수 없습니다."),
    MEMO_NOT_FOUND(NOT_FOUND.getCode(), "메모를 찾을 수 없습니다."),
    ;

    private final int code;
    private final String message;

    @Override
    public CausedBy causedBy() {
        return null;
    }

    @Override
    public String getExplainError() throws NoSuchFieldError {
        return null;
    }
}
