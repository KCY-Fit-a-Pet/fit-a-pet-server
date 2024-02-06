package kr.co.fitapet.domain.domains.member.dto.account;


import kr.co.fitapet.domain.common.annotation.Dto;
import lombok.Builder;
import org.springframework.util.StringUtils;

//@Schema(description = "회원 프로필 조회 응답")
@Builder
@Dto(name = "member")
public record AccountProfileRes(
//        @Schema(description = "회원 번호")
        Long id,
//        @Schema(description = "이름")
        String name,
//        @Schema(description = "닉네임")
        String uid,
//        @Schema(description = "이메일")
        String email,
//        @Schema(description = "프로필 사진")
        String profileImage,
//        @Schema(description = "공지 설정")
        boolean isNotice,
//        @Schema(description = "케어 설정")
        boolean isCare,
//        @Schema(description = "메모 설정")
        boolean isMemo,
//        @Schema(description = "일정 설정")
        boolean isSchedule,
//        @Schema(description = "전화번호")
        String phone
) {
//        public static AccountProfileRes from(Member member) {
//                NotificationSetting setting = member.getNotificationSetting();
//                return AccountProfileRes.builder()
//                        .id(member.getId())
//                        .name(member.getName())
//                        .uid(member.getUid())
//                        .email(StringUtils.hasText(member.getEmail()) ? member.getEmail() : "")
//                        .profileImage(StringUtils.hasText(member.getProfileImg()) ? member.getProfileImg() : "")
//                        .isNotice(setting.getIsNotice())
//                        .isCare(setting.getIsCare())
//                        .isMemo(setting.getIsMemo())
//                        .isSchedule(setting.getIsSchedule())
//                        .phone(member.getPhone())
//                        .build();
//        }
}
