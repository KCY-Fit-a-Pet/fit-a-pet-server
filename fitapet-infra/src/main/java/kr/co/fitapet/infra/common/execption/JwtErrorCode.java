package kr.co.fitapet.infra.common.execption;

import kr.co.fitapet.common.execption.BaseErrorCode;
import kr.co.fitapet.common.execption.CausedBy;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import static kr.co.fitapet.common.execption.StatusCode.*;


@RequiredArgsConstructor
@Getter
public enum JwtErrorCode implements BaseErrorCode {
    /**
     * 400 BAD_REQUEST: 클라이언트의 요청이 부적절 할 경우
     */
    INVALID_HEADER(BAD_REQUEST.getCode(), "유효하지 않은 헤더 포맷입니다"),
    EMPTY_ACCESS_TOKEN(BAD_REQUEST.getCode(), "토큰이 비어있습니다"),

    /**
     * 401 UNAUTHORIZED: 인증되지 않은 사용자
     */
    FAILED_AUTHENTICATION(UNAUTHORIZED.getCode(), "인증에 실패하였습니다"),
    TAMPERED_TOKEN(UNAUTHORIZED.getCode(), "서명이 조작된 토큰입니다"),
    EXPIRED_TOKEN(UNAUTHORIZED.getCode(), "사용기간이 만료된 토큰입니다"),
    MALFORMED_TOKEN(UNAUTHORIZED.getCode(), "비정상적인 토큰입니다"),
    WRONG_JWT_TOKEN(UNAUTHORIZED.getCode(), "잘못된 토큰입니다(default)"),
    UNSUPPORTED_JWT_TOKEN(UNAUTHORIZED.getCode(), "지원하지 않는 토큰입니다"),
    REFRESH_TOKEN_NOT_FOUND(UNAUTHORIZED.getCode(), "없거나 삭제된 리프래시 토큰입니다."),
    USER_NOT_FOUND(UNAUTHORIZED.getCode(), "존재하지 않는 유저입니다"),

    /**
     * 403 FORBIDDEN: 인증된 클라이언트가 권한이 없는 자원에 접근
     */
    FORBIDDEN_ACCESS_TOKEN(FORBIDDEN.getCode(), "해당 토큰에는 엑세스 권한이 없습니다"),
    MISMATCHED_REFRESH_TOKEN(FORBIDDEN.getCode(), "리프레시 토큰의 유저 정보가 일치하지 않습니다"),

    /**
     * 500 INTERNAL_SERVER_ERROR: 서버 내부 에러
     */
    INVALID_TOKEN(INTERNAL_SERVER_ERROR.getCode(), "유효하지 않은 토큰입니다"),
    INVALID_JWT_DTO_FORMAT(INTERNAL_SERVER_ERROR.getCode(), "서버 내부 에러가 발생했습니다."),
    UNEXPECTED_ERROR(INTERNAL_SERVER_ERROR.getCode(), "예상치 못한 에러가 발생했습니다.");

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
