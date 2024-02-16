package kr.co.fitapet.domain.domains.member.exception;

import kr.co.fitapet.common.execption.BaseErrorCode;
import kr.co.fitapet.common.execption.CausedBy;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import static kr.co.fitapet.common.execption.StatusCode.BAD_REQUEST;
import static kr.co.fitapet.common.execption.StatusCode.NOT_FOUND;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum AccountErrorCode implements BaseErrorCode {
    /* 400 BAD REQUEST */
    DUPLICATE_USER_INFO_ERROR(BAD_REQUEST.getCode(), "중복된 유저정보(닉네임/이메일/전화번호)가 존재합니다."),

    DUPLICATE_PHONE_ERROR(BAD_REQUEST.getCode(), "중복된 전화번호가 존재합니다."),

    INVALID_PASSWORD_TYPE_ERROR(BAD_REQUEST.getCode(), "유효하지 않은 비밀번호 타입입니다."),
    NOT_MATCH_PASSWORD_ERROR(BAD_REQUEST.getCode(), "비밀번호가 일치하지 않습니다."),
    INVALID_PASSWORD_REQUEST(BAD_REQUEST.getCode(), "유효하지 않은 비밀번호 변경 요청입니다."),

    NOT_CHANGE_NAME_ERROR(BAD_REQUEST.getCode(), "잘못된 닉네임 변경 요청입니다."),

    INVALID_NOTIFICATION_TYPE_ERROR(BAD_REQUEST.getCode(), "유효하지 않은 알림 타입입니다."),

    MISSMATCH_PHONE_AND_UID_ERROR(BAD_REQUEST.getCode(), "등록된 전화번호와 일치하지 않는 유저입니다."),

    ALREADY_MANAGER_ERROR(BAD_REQUEST.getCode(), "이미 관리자로 등록된 회원입니다."),
    ALREADY_INVITED_ERROR(BAD_REQUEST.getCode(), "이미 초대된 회원입니다."),

    /* 404 NOT FOUND */
    NOT_FOUND_MEMBER_ERROR(NOT_FOUND.getCode(), "존재하지 않는 회원입니다."),
    NOT_FOUND_PHONE_ERROR(NOT_FOUND.getCode(), "존재하지 않는 전화번호입니다."),
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
