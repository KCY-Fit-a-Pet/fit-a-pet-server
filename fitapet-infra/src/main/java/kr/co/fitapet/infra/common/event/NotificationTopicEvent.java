package kr.co.fitapet.infra.common.event;

import kr.co.fitapet.infra.client.fcm.NotificationDataKey;
import lombok.Builder;

import java.util.Map;
import java.util.stream.Collectors;

/**
 * FCM Push Notification Event를 위한 Object
 *
 * <p>
 *     푸시 알림을 받을 토픽을 지정하여 푸시 알림을 보내기 위한 객체입니다.
 *     푸시 알림에는 제목, 내용 및 이미지 URL이 포함될 수 있습니다.
 * </p>
 * @param topic String : 푸시 알림을 받을 토픽
 * @param title String : 푸시 알림 제목
 * @param content String : 푸시 알림 내용
 * @param imageUrl String : 푸시 알림 이미지 URL
 */
@Builder
public record NotificationTopicEvent(
        String topic,
        String title,
        String content,
        String imageUrl,
        Map<NotificationDataKey, String> data
) {
    public Map<String, String> getFormattedData() {
        return data.entrySet().stream()
                .collect(Collectors.toMap(e -> e.getKey().getField(), Map.Entry::getValue));
    }
}
