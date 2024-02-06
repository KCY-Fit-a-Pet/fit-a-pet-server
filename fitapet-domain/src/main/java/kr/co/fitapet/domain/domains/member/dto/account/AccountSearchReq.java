package kr.co.fitapet.domain.domains.member.dto.account;


//@Schema(description = "비밀번호 변경 요청")
public record AccountSearchReq(
//        @NotBlank(message = "전화번호를 입력해주세요.")
//        @Schema(description = "전화번호", example = "01012345678")
        String phone,
//        @Schema(description = "새 비밀번호", example = "1234")
        String newPassword
) {
}
