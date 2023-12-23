package com.kcy.fitapet.global.common.security.oauth;

import com.kcy.fitapet.global.common.security.oauth.dto.OIDCDecodePayload;
import org.springframework.stereotype.Component;

@Component
public interface OauthOIDCProvider {

    String getKidFromUnsignedTokenHeader(String token, String iss, String aud);

    OIDCDecodePayload getOIDCTokenBody(String token, String modulus, String exponent);
}
