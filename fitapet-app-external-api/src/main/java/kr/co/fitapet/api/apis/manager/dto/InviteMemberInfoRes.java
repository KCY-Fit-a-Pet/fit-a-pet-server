package kr.co.fitapet.api.apis.manager.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.fitapet.domain.domains.member.dto.MemberInfo;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
@Schema(description = "매니저 초대 정보 응답")
public record InviteMemberInfoRes(
        @Schema(description = "유저 ID", example = "1")
        Long id,
        @Schema(description = "유저 UID", example = "fitapet")
        String uid,
        @Schema(description = "유저 이름", example = "피타펫")
        String name,
        @Schema(description = "유저 프로필 이미지 URL", example = "https://fitapet.co.kr/fitapet.png")
        String profileImageUrl,
        @Schema(description = "초대 일시", example = "2021-08-01 00:00:00")
        @JsonSerialize(using = LocalDateTimeSerializer.class)
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        LocalDateTime invitedAt,
        @Schema(description = "초대 만료 여부", example = "false")
        Boolean expired
) {
    public static InviteMemberInfoRes valueOf(MemberInfo member, LocalDateTime invitedAt, Boolean expired) {
        return InviteMemberInfoRes.builder()
                .id(member.id())
                .uid(member.uid())
                .name(member.name())
                .profileImageUrl(member.profileImageUrl())
                .invitedAt(invitedAt)
                .expired(expired)
                .build();
    }
}
