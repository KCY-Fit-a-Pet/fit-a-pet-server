package kr.co.fitapet.api.common.event.notification;

import kr.co.fitapet.domain.domains.notification.domain.Notification;
import kr.co.fitapet.domain.domains.notification.type.NotificationType;
import lombok.Builder;

@Builder
public record AnnouncementEvent(
        String title,
        String content,
        String imageUrl
) {
    public Notification toEntity(Long toId) {
        return Notification.builder()
                .title(title)
                .content(content)
                .imageUrl(imageUrl)
                .ctype(NotificationType.ANNOUNCEMENT)
                .toId(toId)
                .build();
    }
}
