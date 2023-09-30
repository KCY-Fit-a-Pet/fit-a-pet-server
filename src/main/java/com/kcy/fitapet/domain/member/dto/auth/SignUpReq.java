package com.kcy.fitapet.domain.member.dto.auth;

import com.kcy.fitapet.domain.member.domain.Member;
import com.kcy.fitapet.domain.member.domain.RoleType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

@Schema(description = "회원가입 요청")
public record SignUpReq(
        @NotNull(message = "Id is is required") @Schema(description = "아이디")
        String uid,
        @NotNull(message = "Password is required") @Schema(description = "비밀번호")
        String password,
        @NotNull(message = "Nickname is required") @Schema(description = "닉네임")
        String name,
        @Email(message = "Invalid Email Format") @Schema(description = "이메일", nullable = true, example = "abc@gmail.com")
        String email,
        @Schema(description = "프로필 이미지", nullable = true)
        String profileImg
) {
    public Member toEntity(String phone) {
        return Member.builder()
                .uid(uid)
                .password(password)
                .name(name)
                .email(email)
                .phone(phone)
                .profileImg(profileImg)
                .accountLocked(Boolean.FALSE)
                .role(RoleType.USER)
                .build();
    }
}
