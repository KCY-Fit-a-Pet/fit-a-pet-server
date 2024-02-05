package kr.co.fitapet.infra.common.util;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;
import kr.co.fitapet.infra.common.execption.JwtErrorCode;
import kr.co.fitapet.infra.common.execption.JwtErrorException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * JWT 예외와 오류 코드를 매핑하는 유틸리티 클래스.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class JwtErrorCodeUtil {
    private static final Map<Class<? extends Exception>, JwtErrorCode> ERROR_CODE_MAP = new HashMap<>();

    // 추가된 예외 유형에 대한 기본 오류 코드를 설정합니다.
    static {
        ERROR_CODE_MAP.put(ExpiredJwtException.class, JwtErrorCode.EXPIRED_TOKEN);
        ERROR_CODE_MAP.put(MalformedJwtException.class, JwtErrorCode.MALFORMED_TOKEN);
        ERROR_CODE_MAP.put(SignatureException.class, JwtErrorCode.TAMPERED_TOKEN);
        ERROR_CODE_MAP.put(UnsupportedJwtException.class, JwtErrorCode.UNSUPPORTED_JWT_TOKEN);
    }

    /**
     * 예외에 해당하는 오류 코드를 반환하거나 기본 오류 코드를 반환합니다.
     *
     * @param exception       발생한 예외
     * @param defaultErrorCode 기본 오류 코드
     * @return 오류 코드
     */
    public static JwtErrorCode determineErrorCode(Exception exception, JwtErrorCode defaultErrorCode) {
        if (exception instanceof JwtErrorException jwtErrorException)
            return jwtErrorException.getErrorCode();

        Class<? extends Exception> exceptionClass = exception.getClass();
        return ERROR_CODE_MAP.getOrDefault(exceptionClass, defaultErrorCode);
    }

    /**
     * 예외에 해당하는 {@link JwtErrorException}을 반환합니다.
     * 기본 오류 코드는 400 UNEXPECTED_ERROR 입니다.
     * 해당 메서드는 {@link #determineErrorCode(Exception, JwtErrorCode)} 메서드를 사용합니다.
     *
     * @param exception 발생한 예외
     * @return AuthErrorException 오류
     */
    public static JwtErrorException determineAuthErrorException(Exception exception) {
        return findAuthErrorException(exception).orElseGet(
                () -> {
                    JwtErrorCode authErrorCode = determineErrorCode(exception, JwtErrorCode.UNEXPECTED_ERROR);
                    return new JwtErrorException(authErrorCode);
                }
        );
    }

    private static Optional<JwtErrorException> findAuthErrorException(Exception exception) {
        if (exception instanceof JwtErrorException) {
            return Optional.of((JwtErrorException) exception);
        } else if (exception.getCause() instanceof JwtErrorException) {
            return Optional.of((JwtErrorException) exception.getCause());
        }
        return Optional.empty();
    }
}
