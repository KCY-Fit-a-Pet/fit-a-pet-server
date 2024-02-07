package kr.co.fitapet.domain.domains.oauth.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;

@Schema(description = "Oauth Sign In Request")
public record OauthSignInReq(
        @Schema(description = "Member Oauth Id")
        @NotEmpty
        String id,
        @Schema(description = "Member Oauth Id Token")
        @NotEmpty
        String idToken,
        @Schema(description = "Member Oauth Nonce")
        @NotEmpty
        String nonce
) {
}
