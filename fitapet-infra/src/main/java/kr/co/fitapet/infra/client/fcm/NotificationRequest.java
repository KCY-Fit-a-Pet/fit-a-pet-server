package kr.co.fitapet.infra.client.fcm;

import com.google.firebase.messaging.Notification;
import lombok.Builder;

@Builder
public record NotificationRequest(
        String token,
        String title,
        String body
) {
    public NotificationRequest {
        if (token == null || token.isBlank()) {
            throw new IllegalArgumentException("token must not be null or empty");
        }
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("title must not be null or empty");
        }
        if (body == null || body.isBlank()) {
            throw new IllegalArgumentException("body must not be null or empty");
        }
    }

    public static NotificationRequest valueOf(String token, String title, String body) {
        return new NotificationRequest(token, title, body);
    }

    public Notification toNotification() {
        return Notification.builder()
                .setTitle(title)
                .setBody(body)
                .build();
    }
}
