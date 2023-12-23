package com.kcy.fitapet.domain.oauth.exception;

import com.kcy.fitapet.global.common.response.code.StatusCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum OauthException implements StatusCode {
    /* BAD REQUEST */
    INVALID_PROVIDER(HttpStatus.BAD_REQUEST, "유효하지 않은 제공자입니다."),

    /* FORBIDDEN */
    NOT_FOUND_MEMBER(HttpStatus.FORBIDDEN, "존재하지 않는 회원입니다.");

    private final HttpStatus httpStatus;
    private final String message;


    @Override
    public String getName() {
        return name();
    }
}
