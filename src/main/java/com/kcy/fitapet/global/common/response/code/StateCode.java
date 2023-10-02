package com.kcy.fitapet.global.common.response.code;

import org.springframework.http.HttpStatus;

public interface StateCode {
    HttpStatus getHttpStatus();
    String getMessage();
}