package kr.co.fitapet.api.apis.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import kr.co.fitapet.domain.domains.member.domain.Member;
import kr.co.fitapet.domain.domains.member.type.RoleType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.StringUtils;

@Schema(description = "회원가입 요청")
public record SignUpReq(
        @NotBlank(message = "Id is is required") @Schema(description = "아이디")
        String uid,
        @NotBlank(message = "Password is required") @Schema(description = "비밀번호")
        String password,
        @NotBlank(message = "Nickname is required") @Schema(description = "닉네임")
        String name,
        @Email(message = "Invalid Email Format") @Schema(description = "이메일", nullable = true, example = "abc@gmail.com")
        String email,
        @Schema(description = "프로필 이미지", nullable = true)
        String profileImg
) {
    public Member toEntity(String phone, PasswordEncoder passwordEncoder) {
        return Member.builder()
                .uid(uid)
                .password(passwordEncoder.encode(password))
                .name(name)
                .email(StringUtils.hasText(email) ? email : null)
                .phone(phone)
                .profileImg(StringUtils.hasText(profileImg) ? profileImg : null)
                .accountLocked(Boolean.FALSE)
                .role(RoleType.USER)
                .build();
    }
}
