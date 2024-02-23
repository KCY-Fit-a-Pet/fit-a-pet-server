package kr.co.fitapet.api.apis.profile.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.naming.OperationNotSupportedException;

@Schema(description = "회원 프로필 수정 요청")
public class ProfilePatchReq {
    @Schema(description = "이름")
    @Getter
    private String name;
    @Schema(description = "현재 비밀번호")
    @Getter
    private String prePassword;
    @Schema(description = "갱신 비밀번호")
    private String newPassword;

    public String getNewEncodedPassword(PasswordEncoder passwordEncoder) {
        return passwordEncoder.encode(newPassword);
    }
}
