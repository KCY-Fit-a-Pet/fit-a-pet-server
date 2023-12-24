package com.kcy.fitapet.global.common.security.oauth.dto;

public record OIDCDecodePayload(
        /* issuer */
        String iss,
        /* client id */
        String aud,
        /* aouth provider account unique id */
        String sub,
        String email
) {
}
