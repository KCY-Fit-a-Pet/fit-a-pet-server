package com.kcy.fitapet.domain.member.dto.profile;

import com.kcy.fitapet.domain.member.domain.Member;
import com.kcy.fitapet.domain.member.type.NotificationSetting;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Schema(description = "회원 프로필 조회 응답")
@Builder
public record MemberProfileRes(
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
        @Schema(description = "알림 설정")
        NotificationSetting notificationSetting,
        @Schema(description = "전화번호")
        String phone
) {
        public static MemberProfileRes from(Member member) {
                return MemberProfileRes.builder()
                        .id(member.getId())
                        .name(member.getName())
                        .uid(member.getUid())
                        .email(member.getEmail())
                        .profileImage(member.getProfileImg())
                        .notificationSetting(member.getNotificationSetting())
                        .phone(member.getPhone())
                        .build();
        }
}
