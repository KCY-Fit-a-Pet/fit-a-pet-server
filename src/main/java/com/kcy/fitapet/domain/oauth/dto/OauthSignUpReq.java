package com.kcy.fitapet.domain.oauth.dto;

import jakarta.validation.constraints.NotBlank;

public record OauthSignUpReq(
        @NotBlank
        String phone,
        @NotBlank
        String name,
        @NotBlank
        String uid
) {
}
