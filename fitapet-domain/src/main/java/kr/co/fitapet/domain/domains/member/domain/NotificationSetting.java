package kr.co.fitapet.domain.domains.member.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import kr.co.fitapet.domain.domains.notification.type.NotificationType;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DynamicInsert
@ToString(of = {"isCare", "isMemo", "isSchedule"})
public class NotificationSetting {
    @Column(name = "is_notice") @ColumnDefault("true")
    private Boolean isNotice = Boolean.TRUE;
    @Column(name = "is_care") @ColumnDefault("true")
    private Boolean isCare = Boolean.TRUE;
    @Column(name = "is_memo") @ColumnDefault("true")
    private Boolean isMemo = Boolean.TRUE;
    @Column(name = "is_schedule") @ColumnDefault("true")
    private Boolean isSchedule = Boolean.TRUE;

    @Builder
    private NotificationSetting(Boolean isNotice, Boolean isCare, Boolean isMemo, Boolean isSchedule) {
        this.isNotice = isNotice;
        this.isCare = isCare;
        this.isMemo = isMemo;
        this.isSchedule = isSchedule;
    }

    public static NotificationSetting of(Boolean isCare, Boolean isMemo, Boolean isSchedule) {
        return NotificationSetting.builder()
                .isCare(isCare)
                .isMemo(isMemo)
                .isSchedule(isSchedule)
                .build();
    }

    public void updateNotificationFromType(NotificationType type) {
        switch (type) {
            case ANNOUNCEMENT -> this.isNotice = !this.isNotice;
            case CARE -> this.isCare = !this.isCare;
            case MEMO -> this.isMemo = !this.isMemo;
            case SCHEDULE -> this.isSchedule = !this.isSchedule;
        }
    }
}
