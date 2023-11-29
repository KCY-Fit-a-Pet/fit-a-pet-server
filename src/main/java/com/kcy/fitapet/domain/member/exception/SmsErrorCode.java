package com.kcy.fitapet.domain.member.exception;

import com.kcy.fitapet.global.common.response.code.StatusCode;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum SmsErrorCode implements StatusCode {
    EXPIRED_AUTH_CODE(BAD_REQUEST, "인증 시간이 만료되었습니다"),
    INVALID_AUTH_CODE(BAD_REQUEST, "유효하지 않은 인증 코드입니다"),
    INVALID_RECEIVER(BAD_REQUEST, "유효하지 않은 수신자입니다");

    private final HttpStatus httpStatus;
    private final String message;

    @Override
    public String getName() {
        return this.name();
    }
}
