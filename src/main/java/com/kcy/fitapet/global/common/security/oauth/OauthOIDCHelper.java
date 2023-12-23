package com.kcy.fitapet.global.common.security.oauth;

import com.kcy.fitapet.global.common.security.oauth.dto.OIDCDecodePayload;
import com.kcy.fitapet.global.common.security.oauth.dto.OIDCPublicKey;
import com.kcy.fitapet.global.common.security.oauth.dto.OIDCPublicKeyResponse;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Helper;

@Helper
@RequiredArgsConstructor
public class OauthOIDCHelper {
    private final OauthOIDCProvider oauthOIDCProvider;

    public OIDCDecodePayload getPayloadFromIdToken(String token, String iss, String aud, OIDCPublicKeyResponse response) {
        String kid = getKidFromUnsignedIdToken(token, iss, aud);

        OIDCPublicKey key = response.getKeys().stream()
                .filter(k -> k.kid().equals(kid))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No matching key found"));

        return oauthOIDCProvider.getOIDCTokenBody(token, key.n(), key.e());
    }

    private String getKidFromUnsignedIdToken(String token, String iss, String aud) {
        return oauthOIDCProvider.getKidFromUnsignedTokenHeader(token, iss, aud);
    }
}
