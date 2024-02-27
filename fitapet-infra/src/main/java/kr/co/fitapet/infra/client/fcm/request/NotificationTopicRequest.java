package kr.co.fitapet.infra.client.fcm.request;

import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import kr.co.fitapet.infra.common.event.NotificationTopicEvent;
import lombok.Builder;

import java.util.Map;

@Builder
public record NotificationTopicRequest(
    String topic,
    String title,
    String content,
    String imageUrl,
    Map<String, String> data
) implements NotificationRequest {
    public NotificationTopicRequest {
        if (topic == null || topic.isBlank()) {
            throw new IllegalArgumentException("topic must not be null or empty");
        }
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("title must not be null or empty");
        }
        if (content == null || content.isBlank()) {
            throw new IllegalArgumentException("body must not be null or empty");
        }
    }

    public static NotificationTopicRequest fromEvent(NotificationTopicEvent event) {
        return NotificationTopicRequest.builder()
                .topic(event.topic())
                .title(event.title())
                .content(event.content())
                .imageUrl(event.imageUrl())
                .data(event.getFormattedData())
                .build();
    }

    public Message.Builder buildSendMessage() {
        return Message.builder()
                .setTopic(topic)
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
