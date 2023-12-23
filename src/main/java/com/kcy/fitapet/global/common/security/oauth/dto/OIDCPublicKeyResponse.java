package com.kcy.fitapet.global.common.security.oauth.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class OIDCPublicKeyResponse {
    List<OIDCPublicKey> keys;
}
