package kr.co.fitapet.domain.domains.member.domain;

import jakarta.persistence.*;
import kr.co.fitapet.domain.common.model.DateAuditable;
import lombok.*;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name = "MEMBER_NICKNAME")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@ToString(of = {"id", "nickname"})
public class MemberNickname extends DateAuditable {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nickname;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "from_id")
    private Member from;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "to_id")
    private Member to;

    @Builder
    private MemberNickname(Member from, Member to, String nickname) {
        this.updateFrom(from);
        this.updateTo(to);
        this.nickname = nickname;
    }

    public static MemberNickname of(Member from, Member to, String nickname) {
        return MemberNickname.builder()
                .from(from)
                .to(to)
                .nickname(nickname)
                .build();
    }

    public void updateAssociation(Member from, Member to) {
        this.updateFrom(from);
        this.updateTo(to);
    }

    private void updateTo(Member to) {
        if (this.to != null) {
            this.to.getToMemberNickname().remove(this);
        }

        this.to = to;

        if (to != null) {
            to.getToMemberNickname().add(this);
        }
    }

    private void updateFrom(Member from) {
        if (this.from != null) {
            this.from.getFromMemberNickname().remove(this);
        }

        this.from = from;

        if (from != null) {
            from.getFromMemberNickname().add(this);
        }
    }

    public void updateNickname(String nickname) {
        this.nickname = nickname;
    }
}
