package kr.co.fitapet.domain.domains.care.exception;

import com.kcy.fitapet.global.common.response.code.StatusCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum CareErrorCode implements StatusCode {
    /* 400 BAD_REQUEST */
    CATEGORY_STATUS_INVALID(HttpStatus.BAD_REQUEST, "카테고리를 생성할 수 없는 상태입니다."),
    ALREADY_CARED(HttpStatus.BAD_REQUEST, "이미 케어한 날짜에 대한 요청"),
    NOT_TODAY_CARE(HttpStatus.BAD_REQUEST, "오늘 날짜에 대한 요청이 아님")
    ;

    private final HttpStatus httpStatus;
    private final String message;

    @Override
    public String getName() {
        return name();
    }
}
