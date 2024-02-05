package kr.co.fitapet.api.common.security.jwt;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum JwtField {
    USER_ID("userId"),
    ROLE("role"),
    PHONE_NUMBER("phoneNumber");

    private final String value;
}
