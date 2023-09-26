package com.kcy.fitapet.domain.member.dto;

import jakarta.validation.constraints.NotNull;

public record SignInDto(
        @NotNull(message = "아이디를 입력해주세요.") String uid,
        @NotNull(message = "비밀번호를 입력해주세요.") String password) {
}
