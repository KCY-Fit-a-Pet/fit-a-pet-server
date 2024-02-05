package kr.co.fitapet.infra.client.oauth.dto;

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
