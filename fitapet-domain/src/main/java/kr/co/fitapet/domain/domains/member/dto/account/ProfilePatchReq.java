package kr.co.fitapet.domain.domains.member.dto.account;

import lombok.Getter;

@Getter
//@Schema(description = "회원 프로필 수정 요청")
public class ProfilePatchReq {
//    @Schema(description = "이름")
//    @NotEmpty
    private String name;
//    @Schema(description = "현재 비밀번호")
//    @NotEmpty
    private String prePassword;
//    @Schema(description = "갱신 비밀번호")
//    @NotEmpty
    private String newPassword;
}
