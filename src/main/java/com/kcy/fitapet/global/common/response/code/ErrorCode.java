package com.kcy.fitapet.global.common.response.code;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public enum ErrorCode implements StateCode {
    /**
     * 400 BAD_QUEST: Client 요청이 잘못된 경우
     */
    BAD_REQUEST_ERROR(BAD_REQUEST, "잘못된 서버 요청입니다."),
    REQUEST_HEADER_MISS_MATCH(BAD_REQUEST, "유효하지 않은 헤더 정보입니다."),
    REQUEST_BODY_MISS_MATCH(BAD_REQUEST,  "유효하지 않은 바디 정보입니다."),
    INVALID_TYPE_VALUE(BAD_REQUEST, "유효하지 않은 타입 필드가 존재합니다."),

    IO_ERROR(BAD_REQUEST, "유효하지 않은 입출력입니다."),

    MISSING_REQUEST_PARAMETER_ERROR(BAD_REQUEST, "요청 파라미터가 전달되지 않았습니다."),
    MISSING_REQUEST_HEADER_ERROR(BAD_REQUEST, "요청 헤더가 전달되지 않았습니다."),
    MISSING_REQUEST_BODY_ERROR(BAD_REQUEST, "요청 바디가 전달되지 않았습니다."),

    DUPLICATE_NICKNAME_ERROR(BAD_REQUEST, "중복된 닉네임이 존재합니다."),
    DUPLICATE_EMAIL_ERROR(BAD_REQUEST, "중복된 이메일이 존재합니다."),
    DUPLICATE_PHONE_ERROR(BAD_REQUEST, "중복된 전화번호가 존재합니다."),

    NOT_MATCH_PASSWORD_ERROR(BAD_REQUEST, "비밀번호가 일치하지 않습니다."),

    EXPIRED_AUTH_CODE(BAD_REQUEST, "인증 코드가 만료되었습니다"),
    INVALID_AUTH_CODE(BAD_REQUEST, "유효하지 않은 인증 코드입니다"),
    INVALID_RECEIVER(BAD_REQUEST, "유효하지 않은 수신자입니다"),

    /**
     * 403 FORBIDDEN: 서버에서 요청을 거부한 경우
     */
    FORBIDDEN_ERROR(FORBIDDEN,"접근 권한이 존재하지 않습니다."),

    /**
     * 404 NOT_FOUND: 서버에서 요청한 자원을 찾을 수 없는 경우
     */
    NOT_FOUND_ERROR(NOT_FOUND,"요청한 자원이 존재하지 않습니다."),
    NULL_POINT_ERROR(NOT_FOUND,"Null Point Exception"),
    NOT_VALID_ERROR(NOT_FOUND,"유효하지 않은 요청입니다."),
    NOT_VALID_HEADER_ERROR(NOT_FOUND,"헤더에 데이터가 존재하지 않습니다."),

    /**
     * 500 INTERNAL_SERVER_ERROR: 서버에서 에러가 발생한 경우
     */
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR,"Internal Server Error Exception"),
    SMS_SEND_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "SMS 전송에 실패하였습니다"),

    INSERT_ERROR(HttpStatus.INTERNAL_SERVER_ERROR,"Insert Transaction Error Exception"),
    UPDATE_ERROR(HttpStatus.INTERNAL_SERVER_ERROR,"Update Transaction Error Exception"),
    DELETE_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Delete Transaction Error Exception"),
    ;

    private final HttpStatus httpStatus;
    private final String message;
}
