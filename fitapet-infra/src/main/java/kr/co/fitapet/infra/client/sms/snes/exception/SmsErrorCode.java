package kr.co.fitapet.infra.client.sms.snes.exception;

import kr.co.fitapet.common.execption.BaseErrorCode;
import kr.co.fitapet.common.execption.CausedBy;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import static kr.co.fitapet.common.execption.StatusCode.BAD_REQUEST;
import static kr.co.fitapet.common.execption.StatusCode.INTERNAL_SERVER_ERROR;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum SmsErrorCode implements BaseErrorCode {
    /* 400 BAD_REQUEST */
    EXPIRED_AUTH_CODE(BAD_REQUEST.getCode(), "인증 시간이 만료되었습니다"),
    INVALID_AUTH_CODE(BAD_REQUEST.getCode(), "유효하지 않은 인증 코드입니다"),
    INVALID_RECEIVER(BAD_REQUEST.getCode(), "유효하지 않은 수신자입니다"),
    NOT_FOUND_SMS_PREFIX(BAD_REQUEST.getCode(), "유효하지 않은 인증 타입입니다."),

    /* 500 INTERNAL_SERVER_ERROR */
    SMS_SEND_ERROR(INTERNAL_SERVER_ERROR.getCode(), "SMS 전송에 실패하였습니다")
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
