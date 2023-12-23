package com.kcy.fitapet.global.common.security.oauth.dto;

public record OIDCPublicKey(
        String kid,
        String kty,
        String alg,
        String use,
        String n,
        String e
) {
}
