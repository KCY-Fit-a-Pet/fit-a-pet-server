package kr.co.fitapet.domain.domains.member.domain;

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