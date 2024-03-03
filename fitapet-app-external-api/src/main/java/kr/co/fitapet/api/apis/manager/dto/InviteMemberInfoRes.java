package kr.co.fitapet.api.apis.manager.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import kr.co.fitapet.domain.domains.invitation.domain.ManagerInvitation;
import kr.co.fitapet.domain.domains.member.dto.MemberInfo;
import kr.co.fitapet.domain.domains.pet.domain.Pet;

import java.time.LocalDateTime;

@Schema(description = "매니저 초대 정보 응답")
public class InviteMemberInfoRes {
    public record ToAspect (
        @NotNull
        InvitationInfo invitation,
        @NotNull
        InvitationPetInfo pet,
        @NotNull
        InvitationMemberInfo member
    ) {
        public static ToAspect valueOf(ManagerInvitation invitation, Pet pet, MemberInfo member) {
            return new ToAspect(
                    InvitationInfo.from(invitation),
                    InvitationPetInfo.from(pet),
                    InvitationMemberInfo.from(member)
            );
        }
    }

    public record FromAspect (
        InvitationInfo invitation,
        InvitationMemberInfo member
    ) {
        public static FromAspect valueOf(ManagerInvitation invitation, MemberInfo member) {
            return new FromAspect(InvitationInfo.from(invitation), InvitationMemberInfo.from(member));
        }
    }

    private record InvitationInfo(
            @Schema(description = "관리자 초대 pk")
            @NotNull
            Long invitationId,
            @Schema(description = "초대 만료 날짜")
            @NotNull
            @JsonSerialize(using = LocalDateTimeSerializer.class)
            @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
            LocalDateTime expireDate
    ) {
        private static InvitationInfo from(ManagerInvitation invitation) {
            return new InvitationInfo(invitation.getId(), invitation.getExpireDate());
        }
    }

    private record InvitationPetInfo(
            @Schema(description = "반려동물 pk")
            @NotNull
            Long petId,
            @Schema(description = "반려동물 이름")
            @NotBlank
            String petName,
            @Schema(description = "반려동물 프로필 사진")
            @NotNull
            String profileImg
    ) {
        private static InvitationPetInfo from(Pet pet) {
            return new InvitationPetInfo(pet.getId(), pet.getPetName(), pet.getPetProfileImg());
        }
    }

    private record InvitationMemberInfo(
            @Schema(description = "유저 pk")
            @NotNull
            Long memberId,
            @Schema(description = "유저 이름(별명)")
            @NotBlank
            String name,
            @Schema(description = "유저 닉네임")
            @NotBlank
            String uid,
            @Schema(description = "유저 프로필 사진")
            @NotBlank
            String profileImg
    ) {
        private static InvitationMemberInfo from(MemberInfo member) {
            return new InvitationMemberInfo(member.id(), member.name(), member.uid(), member.profileImageUrl());
        }
    }
}