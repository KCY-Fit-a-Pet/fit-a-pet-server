package kr.co.fitapet.domain.domains.device.exception;

import kr.co.fitapet.common.execption.BaseErrorCode;
import kr.co.fitapet.common.execption.CausedBy;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import static kr.co.fitapet.common.execption.StatusCode.NOT_FOUND;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum DeviceTokenErrorCode implements BaseErrorCode {
    /* 404 NOT FOUND */
    NOT_FOUND_DEVICE_TOKEN_ERROR(NOT_FOUND.getCode(), "존재하지 않는 디바이스 토큰입니다.");

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
