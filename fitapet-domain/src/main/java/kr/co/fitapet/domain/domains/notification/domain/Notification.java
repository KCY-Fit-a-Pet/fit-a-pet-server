package kr.co.fitapet.domain.domains.notification.domain;

import jakarta.persistence.*;
import kr.co.fitapet.domain.common.model.DateAuditable;
import kr.co.fitapet.domain.domains.member.domain.Member;
import kr.co.fitapet.domain.domains.notification.type.NotificationType;
import kr.co.fitapet.domain.common.converter.NotificationTypeConverter;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;

@Entity
@Table(name = "NOTIFICATION")
@DynamicInsert
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(of = {"title", "content", "ctype", "checked"})
public class Notification extends DateAuditable {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String title;
    private String content;
    @Convert(converter = NotificationTypeConverter.class)
    private NotificationType ctype;
    @ColumnDefault("false")
    private boolean checked;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Builder
    private Notification(String title, String content, NotificationType ctype) {
        this.title = title;
        this.content = content;
        this.ctype = ctype;
    }

    public static Notification of(String title, String content, NotificationType ctype) {
        return Notification.builder()
                .title(title)
                .content(content)
                .ctype(ctype)
                .build();
    }
}
