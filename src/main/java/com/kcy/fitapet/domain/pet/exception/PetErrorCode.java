package com.kcy.fitapet.domain.pet.exception;

import com.kcy.fitapet.global.common.response.code.StatusCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum PetErrorCode implements StatusCode {
    /* 400 BAD_REQUEST */
    ALREADY_CARED(HttpStatus.BAD_REQUEST, "이미 케어한 날짜에 대한 요청"),
    NOT_TODAY_CARE(HttpStatus.BAD_REQUEST, "오늘 날짜에 대한 요청이 아님"),

    /* 403 FORBIDDEN */
    NOT_MANAGER_PET(HttpStatus.FORBIDDEN, "관리자 자격이 없는 반려 동물에 대한 요청");

    private final HttpStatus httpStatus;
    private final String message;

    @Override
    public String getName() {
        return name();
    }
}
