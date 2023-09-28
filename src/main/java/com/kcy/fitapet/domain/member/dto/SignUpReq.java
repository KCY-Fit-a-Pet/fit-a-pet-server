package com.kcy.fitapet.domain.member.dto;

import com.kcy.fitapet.domain.member.domain.Member;
import com.kcy.fitapet.domain.member.domain.RoleType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

public record SignUpReq(
        @NotNull(message = "Id is is required") String uid,
        @NotNull(message = "Password is required") String password,
        @NotNull(message = "Nickname is required") String name,
        @NotNull(message = "Phone number is required") String phone,
        @NotNull(message = "Email is required") @Email(message = "Invalid Email Format") String email,
        String profileImg
) {
    public Member toEntity() {
        return Member.builder()
                .uid(uid)
                .password(password)
                .name(name)
                .phone(phone)
                .email(email)
                .profileImg(profileImg)
                .accountLocked(Boolean.FALSE)
                .role(RoleType.USER)
                .build();
    }
}
