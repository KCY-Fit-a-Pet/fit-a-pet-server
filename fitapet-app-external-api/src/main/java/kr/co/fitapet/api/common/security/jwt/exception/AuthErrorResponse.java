package kr.co.fitapet.api.common.security.jwt.exception;

public record AuthErrorResponse(String code, String message) {
    @Override public String toString() {
        return "AuthErrorResponse(code=" + code + ", message=" + message + ")";
    }
}