package com.kcy.fitapet.domain.notification.domain;

import com.kcy.fitapet.domain.member.domain.Member;
import com.kcy.fitapet.domain.model.Auditable;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

@Entity
@Table(name = "NOTIFICATION_SETTING")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(of = {"isCare", "isMemo", "isSchedule"})
public class NotificationSetting extends Auditable {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "is_care") @ColumnDefault("false")
    private boolean isCare;
    @Column(name = "is_memo") @ColumnDefault("false")
    private boolean isMemo;
    @Column(name = "is_schedule") @ColumnDefault("false")
    private boolean isSchedule;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Builder
    private NotificationSetting(boolean isCare, boolean isMemo, boolean isSchedule) {
        this.isCare = isCare;
        this.isMemo = isMemo;
        this.isSchedule = isSchedule;
    }

    public static NotificationSetting of(boolean isCare, boolean isMemo, boolean isSchedule) {
        return NotificationSetting.builder()
                .isCare(isCare)
                .isMemo(isMemo)
                .isSchedule(isSchedule)
                .build();
    }
}
