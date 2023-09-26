package com.kcy.fitapet.domain.notification.domain;

import com.kcy.fitapet.domain.member.domain.Member;
import com.kcy.fitapet.domain.model.Auditable;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

@Entity
@Table(name = "NOTIFICATION")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(of = {"title", "content", "ctype", "checked"})
public class Notification extends Auditable {
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
    private Notification(String title, String content, NotificationType ctype, Member member) {
        this.title = title;
        this.content = content;
        this.ctype = ctype;
        this.member = member;
    }

    public static Notification of(String title, String content, NotificationType ctype, Member member) {
        return Notification.builder()
                .title(title)
                .content(content)
                .ctype(ctype)
                .member(member)
                .build();
    }
}
