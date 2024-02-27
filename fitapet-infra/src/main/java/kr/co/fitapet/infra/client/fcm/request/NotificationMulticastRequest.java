package kr.co.fitapet.infra.client.fcm.request;

import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.MulticastMessage;
import com.google.firebase.messaging.Notification;
import kr.co.fitapet.infra.common.event.NotificationMulticastEvent;
import lombok.Builder;

import java.util.List;
import java.util.Map;

@Builder
public record NotificationMulticastRequest(
    List<String> tokens,
    String title,
    String content,
    String imageUrl,
    Map<String, String> data
) implements NotificationRequest {
    public NotificationMulticastRequest {
        if (tokens == null) {
            throw new IllegalArgumentException("tokens must not be null");
        }
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("title must not be null or empty");
        }
        if (content == null || content.isBlank()) {
            throw new IllegalArgumentException("body must not be null or empty");
        }
    }

    public static NotificationMulticastRequest fromEvent(NotificationMulticastEvent event) {
        return NotificationMulticastRequest.builder()
                .tokens(event.deviceTokens())
                .title(event.title())
                .content(event.content())
                .imageUrl(event.imageUrl())
                .data(event.getFormattedData())
                .build();
    }

    public MulticastMessage.Builder buildSendMessage() {
        return MulticastMessage.builder()
                .setNotification(toNotification())
                .putAllData(data)
                .addAllTokens(tokens);
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
