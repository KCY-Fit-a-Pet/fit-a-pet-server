package kr.co.fitapet.domain.domains.oauth.dto;

//@Schema(description = "Oauth Sign Up Request")
public record OauthSignUpReq(
//        @Schema(description = "이름")
//        @NotBlank
        String name,
//        @Schema(description = "닉네임")
//        @NotBlank
        String uid,
//        @Schema(description = "Oauth Id Token")
//        @NotBlank
        String idToken,
//        @Schema(description = "Oauth Nonce")
//        @NotBlank
        String nonce
) {
}
