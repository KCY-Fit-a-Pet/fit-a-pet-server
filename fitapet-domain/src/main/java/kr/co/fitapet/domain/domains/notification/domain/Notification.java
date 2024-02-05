package kr.co.fitapet.domain.domains.notification.domain;

import com.kcy.fitapet.domain.member.domain.Member;
import com.kcy.fitapet.domain.model.DateAuditable;
import com.kcy.fitapet.domain.notification.type.NotificationType;
import com.kcy.fitapet.domain.notification.type.converter.NotificationTypeConverter;
import jakarta.persistence.*;
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
