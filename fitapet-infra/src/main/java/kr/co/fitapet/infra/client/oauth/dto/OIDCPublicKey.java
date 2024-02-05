package kr.co.fitapet.infra.client.oauth.dto;

public record OIDCPublicKey(
        String kid,
        String kty,
        String alg,
        String use,
        String n,
        String e
) {
}
