package kr.co.fitapet.domain.domains.manager.dto;

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
    public ManagerInfoRes(Long id, String uid, String name, String profileImageUrl, Boolean isMaster) {
//        this.id = Objects.requireNonNull(id);
//        this.uid = Objects.requireNonNull(uid);
//        this.name = Objects.requireNonNull(name);
//        this.profileImageUrl = Objects.toString(profileImageUrl, "");
//        this.isMaster = Objects.requireNonNull(isMaster);
        this.id = id;
        this.uid = uid;
        this.name = name;
        this.profileImageUrl = Objects.toString(profileImageUrl, "");
        this.isMaster = isMaster;
    }
}
