package kr.co.fitapet.domain.domains.oauth.exception;

import kr.co.fitapet.common.execption.BaseErrorCode;
import kr.co.fitapet.common.execption.CausedBy;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import static kr.co.fitapet.common.execption.StatusCode.BAD_REQUEST;
import static kr.co.fitapet.common.execption.StatusCode.FORBIDDEN;

@Getter
@RequiredArgsConstructor
public enum OauthException implements BaseErrorCode {
    /* BAD REQUEST */
    INVALID_PROVIDER(BAD_REQUEST.getCode(), "유효하지 않은 제공자입니다."),
    INVALID_OAUTH_ID(BAD_REQUEST.getCode(), "ID와 제공자가 일치하지 않습니다."),
    INVALID_OAUTH_PROVIDER(BAD_REQUEST.getCode(), "제공자가 일치하지 않습니다."),

    /* FORBIDDEN */
    NOT_FOUND_MEMBER(FORBIDDEN.getCode(), "존재하지 않는 회원입니다."),
    NOT_FOUND_ID_TOKEN(FORBIDDEN.getCode(), "존재하지 않는 ID Token입니다."),;

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
