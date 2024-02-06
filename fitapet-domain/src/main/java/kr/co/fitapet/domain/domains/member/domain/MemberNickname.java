package kr.co.fitapet.domain.domains.member.domain;

import jakarta.persistence.*;
import kr.co.fitapet.domain.common.model.DateAuditable;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.ToString;

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
