package com.kcy.fitapet.domain.oauth.dto;

import jakarta.validation.constraints.NotEmpty;

public record OauthSignInReq(
        @NotEmpty
        Long id,
        @NotEmpty
        String id_token,
        @NotEmpty
        String nonce
) {
}
