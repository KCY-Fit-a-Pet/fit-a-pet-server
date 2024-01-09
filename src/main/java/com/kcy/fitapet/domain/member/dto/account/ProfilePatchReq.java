package com.kcy.fitapet.domain.member.dto.account;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;

@Getter
@Schema(description = "회원 프로필 수정 요청")
public class ProfilePatchReq {
    @Schema(description = "이름")
    @NotEmpty
    private String name;
    @Schema(description = "현재 비밀번호")
    @NotEmpty
    private String prePassword;
    @Schema(description = "갱신 비밀번호")
    @NotEmpty
    private String newPassword;
}
