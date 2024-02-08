package kr.co.fitapet.api.common.response.code;

import kr.co.fitapet.common.execption.BaseErrorCode;
import kr.co.fitapet.common.execption.CausedBy;
import kr.co.fitapet.common.execption.StatusCode;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import static kr.co.fitapet.common.execption.StatusCode.*;


@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public enum ErrorCode implements BaseErrorCode {
    /**
     * 400 BAD_QUEST: Client 요청이 잘못된 경우
     */
    BAD_REQUEST_ERROR(BAD_REQUEST.getCode(), "잘못된 서버 요청입니다."),
    REQUEST_HEADER_MISS_MATCH(BAD_REQUEST.getCode(), "유효하지 않은 헤더 정보입니다."),
    REQUEST_BODY_MISS_MATCH(BAD_REQUEST.getCode(),  "유효하지 않은 바디 정보입니다."),
    INVALID_QUERY_TYPE(BAD_REQUEST.getCode(), "유효하지 않은 쿼리 타입입니다."),
    INVALID_TYPE_VALUE(BAD_REQUEST.getCode(), "유효하지 않은 타입 필드가 존재합니다."),

    IO_ERROR(BAD_REQUEST.getCode(), "유효하지 않은 입출력입니다."),

    MISSING_REQUEST_PARAMETER_ERROR(BAD_REQUEST.getCode(), "요청 파라미터가 전달되지 않았습니다."),
    MISSING_REQUEST_HEADER_ERROR(BAD_REQUEST.getCode(), "요청 헤더가 전달되지 않았습니다."),
    MISSING_REQUEST_BODY_ERROR(BAD_REQUEST.getCode(), "요청 바디가 전달되지 않았습니다."),

    ALREADY_LOGIN_USER(BAD_REQUEST.getCode(), "이미 로그인한 유저입니다."),

    /**
     * 403 FORBIDDEN: 서버에서 요청을 거부한 경우
     */
    FORBIDDEN_ERROR(FORBIDDEN.getCode(),"접근 권한이 존재하지 않습니다."),

    /**
     * 404 NOT_FOUND: 서버에서 요청한 자원을 찾을 수 없는 경우
     */
    NOT_FOUND_ERROR(NOT_FOUND.getCode(),"요청한 자원이 존재하지 않습니다."),
    NULL_POINT_ERROR(NOT_FOUND.getCode(),"Null Point Exception"),
    NOT_VALID_ERROR(NOT_FOUND.getCode(),"유효하지 않은 요청입니다."),
    NOT_VALID_HEADER_ERROR(NOT_FOUND.getCode(),"헤더에 데이터가 존재하지 않습니다."),

    // ExtendedRepository
    NOT_FOUND_MEMBER(NOT_FOUND.getCode(), "존재하지 않는 유저입니다."),
    NOT_FOUND_MANAGER(NOT_FOUND.getCode(), "존재하지 않는 관리자입니다."),
    NOT_FOUND_PET(NOT_FOUND.getCode(), "존재하지 않는 반려동물입니다."),
    NOT_FOUND_PET_CARE(NOT_FOUND.getCode(), "존재하지 않는 반려동물 돌봄입니다."),
    NOT_FOUND_PET_SCHEDULE(NOT_FOUND.getCode(), "존재하지 않는 반려동물 일정입니다."),
    NOT_FOUND_CARE_DATE(NOT_FOUND.getCode(), "존재하지 않는 돌봄 날짜입니다."),

    /**
     * 500 INTERNAL_SERVER_ERROR: 서버에서 에러가 발생한 경우
     */
    INTERNAL_SERVER_ERROR(StatusCode.INTERNAL_SERVER_ERROR.getCode(), "Internal Server Error Exception"),


    INSERT_ERROR(INTERNAL_SERVER_ERROR.getCode(),"Insert Transaction Error Exception"),
    UPDATE_ERROR(INTERNAL_SERVER_ERROR.getCode(),"Update Transaction Error Exception"),
    DELETE_ERROR(INTERNAL_SERVER_ERROR.getCode(), "Delete Transaction Error Exception"),
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
