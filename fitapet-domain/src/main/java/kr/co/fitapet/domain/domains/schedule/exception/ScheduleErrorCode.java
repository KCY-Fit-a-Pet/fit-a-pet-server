package kr.co.fitapet.domain.domains.schedule.exception;

import com.kcy.fitapet.global.common.response.code.StatusCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ScheduleErrorCode implements StatusCode {
    /* 404 FORBIDDEN */
    SCHEDULE_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 스케줄을 찾을 수 없습니다.")
    ;

    private final HttpStatus httpStatus;
    private final String message;

    @Override
    public String getName() {
        return name();
    }
}
