package com.kcy.fitapet.global.common.response.code;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public enum SuccessCode implements StateCode {
    /**
     * 200 OK:
     */
    SELECT_SUCCESS(OK, "SELECT SUCCESS"),

    /**
     * 201 CREATED:
     */
    INSERT_SUCCESS(CREATED, "INSERT SUCCESS"),
    UPDATE_SUCCESS(CREATED, "UPDATE SUCCESS"),

    /**
     * 204 NO_CONTENT:
     */
    DELETE_SUCCESS(NO_CONTENT, "DELETE SUCCESS"),
    ;

    private final HttpStatus httpStatus;
    private final String message;
}
