package kr.co.fitapet.domain.domains.member.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;
import kr.co.fitapet.domain.common.validator.NotWhiteSpace;

@Schema(description = "닉네임 수정 요청")
public record MemberNicknamePutReq(
        @Schema(description = "닉네임", example = "닉네임", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotWhiteSpace
        @Size(min = 2, max = 20)
        String nickname
) {
}
