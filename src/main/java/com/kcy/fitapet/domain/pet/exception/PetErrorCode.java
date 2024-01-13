package com.kcy.fitapet.domain.pet.exception;

import com.kcy.fitapet.global.common.response.code.StatusCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum PetErrorCode implements StatusCode {
    NOT_MANAGER_PET(HttpStatus.FORBIDDEN, "관리자 자격이 없는 반려 동물에 대한 요청");

    private final HttpStatus httpStatus;
    private final String message;

    @Override
    public String getName() {
        return name();
    }
}
