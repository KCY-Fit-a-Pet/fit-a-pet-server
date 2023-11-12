package com.kcy.fitapet.global.common.util.jwt.exception;

import com.kcy.fitapet.global.common.response.code.StatusCode;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public enum AuthErrorCode implements StatusCode {
    /**
     * 400 BAD_REQUEST: 클라이언트의 요청이 부적절 할 경우
     */
    INVALID_HEADER(BAD_REQUEST, "유효하지 않은 헤더 포맷입니다"),
    EMPTY_ACCESS_TOKEN(BAD_REQUEST, "토큰이 비어있습니다"),

    /**
     * 401 UNAUTHORIZED: 인증되지 않은 사용자
     */
    FAILED_AUTHENTICATION(UNAUTHORIZED, "인증에 실패하였습니다"),
    TAMPERED_ACCESS_TOKEN(UNAUTHORIZED, "서명이 조작된 토큰입니다"),
    EXPIRED_ACCESS_TOKEN(UNAUTHORIZED, "사용기간이 만료된 토큰입니다"),
    MALFORMED_ACCESS_TOKEN(UNAUTHORIZED, "비정상적인 토큰입니다"),
    WRONG_JWT_TOKEN(UNAUTHORIZED, "잘못된 토큰입니다(default)"),
    UNSUPPORTED_JWT_TOKEN(UNAUTHORIZED, "지원하지 않는 토큰입니다"),
    REFRESH_TOKEN_NOT_FOUND(UNAUTHORIZED, "없거나 삭제된 리프래시 토큰입니다."),
    USER_NOT_FOUND(UNAUTHORIZED, "존재하지 않는 유저입니다"),

    /**
     * 403 FORBIDDEN: 인증된 클라이언트가 권한이 없는 자원에 접근
     */
    FORBIDDEN_ACCESS_TOKEN(FORBIDDEN, "해당 토큰에는 엑세스 권한이 없습니다"),
    MISMATCHED_REFRESH_TOKEN(FORBIDDEN, "리프레시 토큰의 유저 정보가 일치하지 않습니다"),

    /**
     * 500 INTERNAL_SERVER_ERROR: 서버 내부 에러
     */
    INVALID_JWT_DTO_FORMAT(INTERNAL_SERVER_ERROR, "서버 내부 에러가 발생했습니다."),
    UNEXPECTED_ERROR(INTERNAL_SERVER_ERROR, "예상치 못한 에러가 발생했습니다."),;

    private final HttpStatus httpStatus;
    private final String message;

    @Override
    public String getName() {
        return name();
    }
}
