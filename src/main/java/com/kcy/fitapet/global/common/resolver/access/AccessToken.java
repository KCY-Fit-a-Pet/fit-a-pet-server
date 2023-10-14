package com.kcy.fitapet.global.common.resolver.access;

import java.time.LocalDateTime;
import java.util.Date;

public record AccessToken(
        String accessToken,
        Long userId,
        LocalDateTime expiryDate,
        boolean isReissued
) {
    public static AccessToken of(String accessToken, Long userId, LocalDateTime expiryDate, boolean isReissued) {
        return new AccessToken(accessToken, userId, expiryDate, isReissued);
    }
}
