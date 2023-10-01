package com.kcy.fitapet.domain.member.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

@Schema(description = "로그인 요청")
public record SignInReq(
        @NotNull(message = "아이디를 입력해주세요.") @Schema(description = "아이디")
        String uid,
        @NotNull(message = "비밀번호를 입력해주세요.") @Schema(description = "비밀번호")
        String password) {
}
