package kr.co.fitapet.domain.domains.memo.exception;

import kr.co.fitapet.common.execption.BaseErrorCode;
import kr.co.fitapet.common.execption.CausedBy;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import static kr.co.fitapet.common.execption.StatusCode.NOT_FOUND;

@Getter
@RequiredArgsConstructor
public enum MemoErrorCode implements BaseErrorCode {

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
