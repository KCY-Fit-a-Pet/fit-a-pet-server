package com.kcy.fitapet.domain.member.exception;

import com.kcy.fitapet.global.common.response.code.StatusCode;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum AccountErrorCode implements StatusCode {
    DUPLICATE_USER_INFO_ERROR(BAD_REQUEST, "중복된 유저정보(닉네임/이메일/전화번호)가 존재합니다."),

    DUPLICATE_PHONE_ERROR(BAD_REQUEST, "중복된 전화번호가 존재합니다."),

    INVALID_PASSWORD_TYPE_ERROR(BAD_REQUEST, "유효하지 않은 비밀번호 타입입니다."),
    NOT_MATCH_PASSWORD_ERROR(BAD_REQUEST, "비밀번호가 일치하지 않습니다."),
    INVALID_PASSWORD_REQUEST(BAD_REQUEST, "유효하지 않은 비밀번호 변경 요청입니다."),

    NOT_CHANGE_NAME_ERROR(BAD_REQUEST, "잘못된 닉네임 변경 요청입니다."),

    INVALID_NOTIFICATION_TYPE_ERROR(BAD_REQUEST, "유효하지 않은 알림 타입입니다."),

    /* 404 */
    NOT_FOUND_MEMBER_ERROR(NOT_FOUND, "존재하지 않는 회원입니다."),
    NOT_FOUND_PHONE_ERROR(NOT_FOUND, "존재하지 않는 전화번호입니다."),
    ;

    private final HttpStatus httpStatus;
    private final String message;

    @Override
    public String getName() {
        return this.name();
    }
}
