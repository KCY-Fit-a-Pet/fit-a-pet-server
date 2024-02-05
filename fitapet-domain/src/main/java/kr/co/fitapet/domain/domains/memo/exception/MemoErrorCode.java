package kr.co.fitapet.domain.domains.memo.exception;

import com.kcy.fitapet.global.common.response.code.StatusCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum MemoErrorCode implements StatusCode {

    /* 404 NOT_FOUND */
    MEMO_CATEGORY_NOT_FOUND(HttpStatus.NOT_FOUND, "메모 카테고리를 찾을 수 없습니다."),
    MEMO_NOT_FOUND(HttpStatus.NOT_FOUND, "메모를 찾을 수 없습니다."),
    ;

    private final HttpStatus httpStatus;
    private final String message;

    @Override
    public String getName() {
        return name();
    }
}
