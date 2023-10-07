package com.kcy.fitapet.global.common.resolver.access;

import java.util.Date;

public record AccessToken(
        String accessToken,
        Long userId,
        Date expiryDate
) {
}
