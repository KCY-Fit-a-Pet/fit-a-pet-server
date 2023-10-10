package com.kcy.fitapet.global.common.util.exception;

import com.kcy.fitapet.global.common.util.jwt.exception.AuthErrorCode;
import com.kcy.fitapet.global.common.util.jwt.exception.AuthErrorException;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class JwtErrorCodeUtil {
    private static final Map<Class<? extends Exception>, AuthErrorCode> ERROR_CODE_MAP = new HashMap<>();

    // 추가된 예외 유형에 대한 기본 오류 코드를 설정합니다.
    static {
        ERROR_CODE_MAP.put(ExpiredJwtException.class, AuthErrorCode.EXPIRED_ACCESS_TOKEN);
        ERROR_CODE_MAP.put(MalformedJwtException.class, AuthErrorCode.MALFORMED_ACCESS_TOKEN);
        ERROR_CODE_MAP.put(SignatureException.class, AuthErrorCode.TAMPERED_ACCESS_TOKEN);
        ERROR_CODE_MAP.put(UnsupportedJwtException.class, AuthErrorCode.UNSUPPORTED_JWT_TOKEN);
    }

    /**
     * 예외에 해당하는 오류 코드를 반환하거나 기본 오류 코드를 반환합니다.
     *
     * @param exception       발생한 예외
     * @param defaultErrorCode 기본 오류 코드
     * @return 오류 코드
     */
    public static AuthErrorCode determineErrorCode(Exception exception, AuthErrorCode defaultErrorCode) {
        if (exception instanceof AuthErrorException authErrorException)
            return authErrorException.getErrorCode();

        Class<? extends Exception> exceptionClass = exception.getClass();
        return ERROR_CODE_MAP.getOrDefault(exceptionClass, defaultErrorCode);
    }

    /**
     * 예외에 해당하는 오류 코드를 반환하거나 기본 오류 코드를 반환합니다.
     * 기본 오류 코드는 400 UNEXPECTED_ERROR 입니다.
     *
     * @param exception 발생한 예외
     * @return 오류 코드
     */
    public static AuthErrorCode determineErrorCode(Exception exception) {
        return determineErrorCode(exception, AuthErrorCode.UNEXPECTED_ERROR);
    }
}
