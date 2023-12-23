package com.kcy.fitapet.domain.member.dto.account;

import com.kcy.fitapet.domain.member.domain.Member;
import com.kcy.fitapet.domain.member.domain.NotificationSetting;
import com.kcy.fitapet.global.common.util.bind.Dto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Schema(description = "회원 프로필 조회 응답")
@Builder
@Dto(name = "member")
public record AccountProfileRes(
        @Schema(description = "회원 번호")
        Long id,
        @Schema(description = "이름")
        String name,
        @Schema(description = "닉네임")
        String uid,
        @Schema(description = "이메일")
        String email,
        @Schema(description = "프로필 사진")
        String profileImage,
        @Schema(description = "공지 설정")
        boolean isNotice,
        @Schema(description = "케어 설정")
        boolean isCare,
        @Schema(description = "메모 설정")
        boolean isMemo,
        @Schema(description = "일정 설정")
        boolean isSchedule,
        @Schema(description = "전화번호")
        String phone
) {
        public static AccountProfileRes from(Member member) {
                NotificationSetting setting = member.getNotificationSetting();
                return AccountProfileRes.builder()
                        .id(member.getId())
                        .name(member.getName())
                        .uid(member.getUid())
                        .email(member.getEmail())
                        .profileImage(member.getProfileImg())
                        .isNotice(setting.isNotice())
                        .isCare(setting.isCare())
                        .isMemo(setting.isMemo())
                        .isSchedule(setting.isSchedule())
                        .phone(member.getPhone())
                        .build();
        }
}
