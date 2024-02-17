package kr.co.fitapet.domain.domains.member.dto;

import lombok.Builder;

import java.util.Objects;

@Builder
public record MemberInfo(
        Long id,
        String uid,
        String name,
        String profileImageUrl
) {
    public MemberInfo(Long id, String uid, String name, String profileImageUrl) {
        this.id = Objects.requireNonNull(id);
        this.uid = Objects.requireNonNull(uid);
        this.name = Objects.requireNonNull(name);
        this.profileImageUrl = Objects.toString(profileImageUrl, "");
    }
}
