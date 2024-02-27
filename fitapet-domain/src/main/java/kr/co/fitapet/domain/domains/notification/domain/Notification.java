package kr.co.fitapet.domain.domains.notification.domain;

import jakarta.persistence.*;
import kr.co.fitapet.domain.common.model.DateAuditable;
import kr.co.fitapet.domain.domains.member.domain.Member;
import kr.co.fitapet.domain.domains.notification.type.NotificationType;
import kr.co.fitapet.domain.common.converter.NotificationTypeConverter;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;

@Getter
@Entity
@Table(name = "NOTIFICATION")
@DynamicInsert
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(of = {"title", "content", "ctype", "checked"})
@EqualsAndHashCode(of = {"id", "fromId", "toId", "domainId", "subjectId"}, callSuper = false)
public class Notification extends DateAuditable {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String content;
    private String imageUrl;
    @Convert(converter = NotificationTypeConverter.class)
    private NotificationType ctype;
    @ColumnDefault("false")
    private boolean checked;

    private Long fromId;
    private Long toId;
    private Long domainId;
    private Long subjectId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Builder(builderClassName = "NotificationBuilder")
    private Notification(String title, String content, NotificationType ctype, String imageUrl, Long fromId, Long toId, Long domainId, Long subjectId, Member member) {
        this.title = title;
        this.content = content;
        this.ctype = ctype;
        this.imageUrl = imageUrl;
        this.fromId = fromId;
        this.toId = toId;
        this.domainId = domainId;
        this.subjectId = subjectId;
        this.member = member;
    }
}
