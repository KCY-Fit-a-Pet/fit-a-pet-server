package com.kcy.fitapet.domain.oauth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Oauth Sign Up Request")
public record OauthSignUpReq(
        @Schema(description = "전화번호")
        @NotBlank
        String phone,
        @Schema(description = "이름")
        @NotBlank
        String name,
        @Schema(description = "닉네임")
        @NotBlank
        String uid,
        @Schema(description = "Oauth Id Token")
        @NotBlank
        String idToken,
        @Schema(description = "Oauth Nonce")
        @NotBlank
        String nonce
) {
}
