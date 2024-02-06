package kr.co.fitapet.domain.domains.schedule.exception;

import kr.co.fitapet.common.execption.BaseErrorCode;
import kr.co.fitapet.common.execption.CausedBy;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import static kr.co.fitapet.common.execption.StatusCode.NOT_FOUND;

@Getter
@RequiredArgsConstructor
public enum ScheduleErrorCode implements BaseErrorCode {
    /* 404 FORBIDDEN */
    SCHEDULE_NOT_FOUND(NOT_FOUND.getCode(), "해당 스케줄을 찾을 수 없습니다.")
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
