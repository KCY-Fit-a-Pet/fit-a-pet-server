package com.kcy.fitapet.domain.oauth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Oauth Sign In Request")
public record OauthSignInReq(
        @Schema(description = "Member Oauth Id")
        @NotNull
        Long id,
        @Schema(description = "Member Oauth Id Token")
        @NotEmpty
        String idToken,
        @Schema(description = "Member Oauth Nonce")
        @NotEmpty
        String nonce
) {
}
