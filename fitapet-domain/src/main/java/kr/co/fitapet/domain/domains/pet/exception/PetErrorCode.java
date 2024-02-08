package kr.co.fitapet.domain.domains.pet.exception;

import kr.co.fitapet.common.execption.BaseErrorCode;
import kr.co.fitapet.common.execption.CausedBy;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import static kr.co.fitapet.common.execption.StatusCode.FORBIDDEN;

@Getter
@RequiredArgsConstructor
public enum PetErrorCode implements BaseErrorCode {
    /* 403 FORBIDDEN */
    NOT_MANAGER_PET(FORBIDDEN.getCode(), "관리자 자격이 없는 반려 동물에 대한 요청");

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
