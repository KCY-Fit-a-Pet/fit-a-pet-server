package kr.co.fitapet.infra.client.fcm;

import lombok.Builder;

import java.util.List;
import java.util.Map;

/**
 * FCM Push Notification Event를 위한 Object
 *
 * <p>
 *     하나 이상의 디바이스에 푸시 알림을 보내기 위한 객체입니다.
 *     푸시 알림은 단일 디바이스, 여러 디바이스 또는 토픽으로 보낼 수 있습니다.
 *     푸시 알림에는 제목, 내용 및 이미지 URL이 포함될 수 있습니다.
 *     푸시 알림이 토픽으로 보내지는 경우 deviceTokens 필드는 null이어야 합니다.
 * </p>
 * @param deviceTokens List<String> : 푸시 알림을 받을 디바이스 토큰 리스트
 * @param title String : 푸시 알림 제목
 * @param topic String : 푸시 알림을 받을 토픽
 * @param content String : 푸시 알림 내용
 * @param imageUrl String : 푸시 알림 이미지 URL
 */
@Builder
public record NotificationEvent(
        List<String> deviceTokens,
        String title,
        String topic,
        String content,
        String imageUrl,
        Map<String, String> data
) {
    public boolean isTopic() {
        return this.deviceTokens == null;
    }

    public boolean isMulticast() {
        return this.deviceTokens() != null && this.deviceTokens().size() > 1;
    }
}
