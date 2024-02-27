package kr.co.fitapet.api.apis.profile.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import kr.co.fitapet.domain.domains.device.domain.DeviceToken;
import kr.co.fitapet.domain.domains.member.domain.Member;

@Schema(description = "디바이스 토큰 등록 요청")
public record DeviceTokenReq(
        @Schema(description = "디바이스 토큰", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank
        String deviceToken,
        @Schema(description = "디바이스 OS", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank
        String os,
        @Schema(description = "디바이스 모델명", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank
        String deviceModel
) {
        public DeviceToken toEntity(Member member) {
                return DeviceToken.of(deviceToken, os, deviceModel, member);
        }
}
