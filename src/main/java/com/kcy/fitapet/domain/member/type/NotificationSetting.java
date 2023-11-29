package com.kcy.fitapet.domain.member.type;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(of = {"isCare", "isMemo", "isSchedule"})
public class NotificationSetting {
    @Column(name = "is_care") @ColumnDefault("false")
    private boolean isCare;
    @Column(name = "is_memo") @ColumnDefault("false")
    private boolean isMemo;
    @Column(name = "is_schedule") @ColumnDefault("false")
    private boolean isSchedule;

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

    public void updateEmailNotification() {
        this.isCare = !this.isCare;
    }

    public void updateMemoNotification() {
        this.isMemo = !this.isMemo;
    }

    public void updateScheduleNotification() {
        this.isSchedule = !this.isSchedule;
    }
}
