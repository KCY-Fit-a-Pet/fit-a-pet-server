package kr.co.fitapet.api.common.response.code;

import org.springframework.http.HttpStatus;

public interface StatusCode {
    HttpStatus getHttpStatus();
    String getMessage();
    String getName();
}