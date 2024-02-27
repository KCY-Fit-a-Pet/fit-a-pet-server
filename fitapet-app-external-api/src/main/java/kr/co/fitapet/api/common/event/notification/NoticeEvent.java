package kr.co.fitapet.api.common.event.notification;

import kr.co.fitapet.infra.client.fcm.NotificationDataKey;
import lombok.Builder;

import java.util.Map;

@Builder
public record NoticeEvent(
        NoticeType noticeType,
        Map<NotificationDataKey, ?> data
) {
}
