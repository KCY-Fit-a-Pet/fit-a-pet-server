package kr.co.fitapet.domain.domains.oauth.dto;


//@Schema(description = "OAuth 전화 번호 인증 요청 정보")
public record OauthSmsReq(
//        @Schema(description = "전화 번호", example = "01012345678")
//        @NotNull(message = "전화 번호는 필수 입력값입니다.")
        String to,
//        @Schema(description = "OIDC 토큰")
//        @NotNull(message = "OIDC 토큰은 필수 입력값입니다.")
        String idToken,
//        @Schema(description = "OIDC 토큰 유효성 검사를 위한 nonce")
//        @NotNull(message = "nonce는 필수 입력값입니다.")
        String nonce
) {
//        public SmsReq toSmsReq() {
//                return SmsReq.builder()
//                        .to(this.to)
//                        .build();
//        }
}
