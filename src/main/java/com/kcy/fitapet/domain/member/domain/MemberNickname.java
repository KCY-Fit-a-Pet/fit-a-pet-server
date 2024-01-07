package com.kcy.fitapet.domain.member.domain;

import com.kcy.fitapet.domain.model.DateAuditable;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "MEMBER_NICKNAME")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(of = {"id", "nickname"})
public class MemberNickname extends DateAuditable {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nickname;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "from")
    private Member from;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "to")
    private Member to;

    @Builder
    private MemberNickname(String nickname) {
        this.nickname = nickname;
    }

    public static MemberNickname of(String nickname) {
        return MemberNickname.builder()
                .nickname(nickname)
                .build();
    }
}
