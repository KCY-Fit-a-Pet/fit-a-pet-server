package kr.co.fitapet.domain.common.redis;

import java.time.LocalDateTime;

public record AccessToken(
        String accessToken,
        Long userId,
        LocalDateTime expiryDate
) {
    public static AccessToken of(String accessToken, Long userId, LocalDateTime expiryDate) {
        return new AccessToken(accessToken, userId, expiryDate);
    }
}