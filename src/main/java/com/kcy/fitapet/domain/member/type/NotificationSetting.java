package com.kcy.fitapet.domain.member.type;

import com.kcy.fitapet.domain.notification.type.NotificationType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(of = {"isCare", "isMemo", "isSchedule"})
public class NotificationSetting {
    @Column(name = "is_notice") @ColumnDefault("true")
    private boolean isNotice;
    @Column(name = "is_care") @ColumnDefault("true")
    private boolean isCare;
    @Column(name = "is_memo") @ColumnDefault("true")
    private boolean isMemo;
    @Column(name = "is_schedule") @ColumnDefault("true")
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

    public void updateNotificationFromType(NotificationType type) {
        switch (type) {
            case NOTICE:
                this.isNotice = !this.isNotice;
                break;
            case CARE:
                this.isCare = !this.isCare;
                break;
            case MEMO:
                this.isMemo = !this.isMemo;
                break;
            case SCHEDULE:
                this.isSchedule = !this.isSchedule;
                break;
        }
    }
}
