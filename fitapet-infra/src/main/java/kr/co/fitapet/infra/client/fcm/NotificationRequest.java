package kr.co.fitapet.infra.client.fcm;

import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.MulticastMessage;
import com.google.firebase.messaging.Notification;
import lombok.Builder;

import java.util.List;
import java.util.Map;

/**
 * FCM 메시지 전송 요청
 * @param tokens : 메시지를 받을 디바이스 토큰 리스트
 * @param topic : 메시지를 받을 토픽 (tokens가 없을 때만 사용 가능)
 * @param title : 메시지 제목
 * @param body : 메시지 내용
 */
@Builder
public record NotificationRequest(
        List<String> tokens,
        String topic,
        String title,
        String body,
        String imageUrl,
        Map<String, String> data
) {
    public NotificationRequest {
        // tokens가 있으면 topic은 없어야 한다. 역도 성립
        if ((tokens != null && topic != null) || (tokens == null && topic == null)) {
            throw new IllegalArgumentException("tokens and topic must not be both null or both not null");
        }
        if (topic == null && tokens.isEmpty()) {
            throw new IllegalArgumentException("tokens must not be null or empty");
        }
        if (tokens == null && topic.isBlank()) {
            throw new IllegalArgumentException("topic must not be null or empty");
        }
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("title must not be null or empty");
        }
        if (body == null || body.isBlank()) {
            throw new IllegalArgumentException("body must not be null or empty");
        }
    }

    public static NotificationRequest valueOf(List<String> tokens, String title, String body, String imageUrl, Map<String, String> data) {
        return new NotificationRequest(tokens, null, title, body, imageUrl, data);
    }

    public static NotificationRequest valueOf(String topic, String title, String body, String imageUrl, Map<String, String> data) {
        return new NotificationRequest(null, topic, title, body, imageUrl, data);
    }

    public static NotificationRequest fromEvent(NotificationEvent event) {
        return (event.isTopic()) ?
                valueOf(event.topic(), event.title(), event.content(), event.imageUrl(), event.data()) :
                valueOf(event.deviceTokens(), event.title(), event.content(), event.imageUrl(), event.data());
    }

    public Message.Builder buildSendMessageToToken() {
        return Message.builder()
                .setToken(tokens.get(0))
                .putAllData(data)
                .setNotification(toNotification());
    }

    public MulticastMessage.Builder buildSendMessagesToTokens() {
        return MulticastMessage.builder()
                .setNotification(toNotification())
                .putAllData(data)
                .addAllTokens(tokens);
    }

    public Message.Builder buildSendMessageToTopic() {
        return Message.builder()
                .setNotification(toNotification())
                .putAllData(data)
                .setTopic(topic);
    }

    private Notification toNotification() {
        return Notification.builder()
                .setTitle(title)
                .setBody(body)
                .setImage(imageUrl)
                .build();
    }
}
