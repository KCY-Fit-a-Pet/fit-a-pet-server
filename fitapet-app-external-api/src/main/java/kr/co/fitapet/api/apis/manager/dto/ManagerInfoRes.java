package kr.co.fitapet.api.apis.manager.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.fitapet.domain.domains.member.domain.Member;
import lombok.Builder;

import java.util.Objects;

@Builder
@Schema(description = "매니저 정보 응답")
public record ManagerInfoRes(
        @Schema(description = "매니저 ID", example = "1")
        Long id,
        @Schema(description = "매니저 UID", example = "fitapet")
        String uid,
        @Schema(description = "매니저 이름", example = "피타펫")
        String name,
        @Schema(description = "매니저 프로필 이미지 URL", example = "https://fitapet.co.kr/fitapet.png")
        String profileImageUrl,
        @Schema(description = "마스터 여부", example = "true")
        Boolean isMaster
) {
    public static ManagerInfoRes valueOf(Member member, Long masterId) {
        return ManagerInfoRes.builder()
                .id(member.getId())
                .uid(member.getUid())
                .name(member.getName())
                .profileImageUrl(Objects.toString(member.getProfileImg(), ""))
                .isMaster(member.getId().equals(masterId))
                .build();
    }
}
