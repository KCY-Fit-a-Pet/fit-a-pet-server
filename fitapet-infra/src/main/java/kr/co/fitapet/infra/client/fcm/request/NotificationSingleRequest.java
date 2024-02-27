package kr.co.fitapet.infra.client.fcm.request;

import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.MulticastMessage;
import com.google.firebase.messaging.Notification;
import kr.co.fitapet.infra.common.event.NotificationSingleEvent;
import lombok.Builder;

import java.util.Map;

/**
 * FCM 메시지 전송 요청
 * @param tokens : 메시지를 받을 디바이스 토큰 리스트
 * @param topic : 메시지를 받을 토픽 (tokens가 없을 때만 사용 가능)
 * @param title : 메시지 제목
 * @param content : 메시지 내용
 */
@Builder
public record NotificationSingleRequest(
        String token,
        String title,
        String content,
        String imageUrl,
        Map<String, String> data
) implements NotificationRequest {
    public NotificationSingleRequest {
        if (token == null || token.isBlank()) {
            throw new IllegalArgumentException("token must not be null or empty");
        }
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("title must not be null or empty");
        }
        if (content == null || content.isBlank()) {
            throw new IllegalArgumentException("body must not be null or empty");
        }
    }

    public static NotificationSingleRequest fromEvent(NotificationSingleEvent event) {
        return NotificationSingleRequest.builder()
                .token(event.deviceToken())
                .title(event.title())
                .content(event.content())
                .imageUrl(event.imageUrl())
                .data(event.getFormattedData())
                .build();
    }

    public Message.Builder buildSendMessage() {
        return Message.builder()
                .setToken(token)
                .putAllData(data)
                .setNotification(toNotification());
    }

    @Override
    public Notification toNotification() {
        return Notification.builder()
                .setTitle(title)
                .setBody(content)
                .setImage(imageUrl)
                .build();
    }
}
