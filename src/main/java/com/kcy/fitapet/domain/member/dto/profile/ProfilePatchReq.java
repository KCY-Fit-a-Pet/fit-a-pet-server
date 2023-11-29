package com.kcy.fitapet.domain.member.dto.profile;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
@Schema(description = "회원 프로필 수정 요청")
public class ProfilePatchReq {
    @Schema(description = "이름")
    private String name;
    @Schema(description = "현재 비밀번호")
    private String prePassword;
    @Schema(description = "갱신 비밀번호")
    private String newPassword;
}
