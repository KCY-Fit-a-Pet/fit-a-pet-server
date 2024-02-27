package kr.co.fitapet.api.common.event.notification;

import kr.co.fitapet.domain.domains.notification.domain.Notification;
import kr.co.fitapet.domain.domains.notification.type.NotificationType;
import kr.co.fitapet.infra.client.fcm.NotificationDataKey;
import lombok.Builder;

import java.util.Map;
import java.util.Objects;

@Builder
public record NoticeEvent(
        NoticeType noticeType,
        NotificationType notificationType,
        String title,
        String content,
        String imageUrl,
        Map<NotificationDataKey, String> data
) {
    public Notification toEntity() {
        String fromId = data.getOrDefault(NotificationDataKey.FROM_ID, null);
        String toId = data.getOrDefault(NotificationDataKey.TO_ID, null);
        String domainId = data.getOrDefault(NotificationDataKey.DOMAIN_ID, null);
        String subjectId = data.getOrDefault(NotificationDataKey.SUBJECT_ID, null);

        return Notification.builder()
                .title(title)
                .content(content)
                .imageUrl(imageUrl)
                .ctype(notificationType)
                .fromId(Objects.isNull(fromId) ? null : Long.parseLong(fromId))
                .toId(Objects.isNull(toId) ? null : Long.parseLong(toId))
                .domainId(Objects.isNull(domainId) ? null : Long.parseLong(domainId))
                .subjectId(Objects.isNull(subjectId) ? null : Long.parseLong(subjectId))
                .build();
    }

    public String getValueFromNotificationDataKey(NotificationDataKey key) {
        return data.getOrDefault(key, null);
    }
}
