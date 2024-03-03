package kr.co.fitapet.domain.common.exception;

import kr.co.fitapet.common.execption.BaseErrorCode;
import kr.co.fitapet.common.execption.CausedBy;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import static kr.co.fitapet.common.execption.StatusCode.NOT_FOUND;

@Getter
@RequiredArgsConstructor
public enum DomainErrorCode implements BaseErrorCode {
    NOT_FOUND_MEMBER(NOT_FOUND.getCode(), "존재하지 않는 유저입니다."),
    NOT_FOUND_MANAGER(NOT_FOUND.getCode(), "존재하지 않는 관리자입니다."),
    NOT_FOUND_PET(NOT_FOUND.getCode(), "존재하지 않는 반려동물입니다."),
    NOT_FOUND_PET_CARE(NOT_FOUND.getCode(), "존재하지 않는 반려동물 돌봄입니다."),
    NOT_FOUND_PET_SCHEDULE(NOT_FOUND.getCode(), "존재하지 않는 반려동물 일정입니다."),
    NOT_FOUND_SCHEDULE(NOT_FOUND.getCode(), "존재하지 않는 일정입니다."),
    NOT_FOUND_CARE_CATEGORY(NOT_FOUND.getCode(), "존재하지 않는 돌봄 카테고리입니다."),
    NOT_FOUND_CARE_DATE(NOT_FOUND.getCode(), "존재하지 않는 돌봄 날짜입니다."),
    NOT_FOUND_CARE_LOG(NOT_FOUND.getCode(), "존재하지 않는 돌봄 로그입니다."),
    NOT_FOUND_NOTIFICATION(NOT_FOUND.getCode(), "존재하지 않는 알림입니다."),
    NOT_FOUND_OAUTH(NOT_FOUND.getCode(), "존재하지 않는 OAuth입니다."),
    NOT_FOUND_DEVICE_TOKEN(NOT_FOUND.getCode(), "존재하지 않는 디바이스 토큰입니다."),
    NOT_FOUND_MANAGER_INVITATION(NOT_FOUND.getCode(), "존재하지 않는 관리자 초대입니다."),
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
