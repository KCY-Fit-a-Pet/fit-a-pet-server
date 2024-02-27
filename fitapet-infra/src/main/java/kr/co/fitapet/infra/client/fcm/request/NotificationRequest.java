package kr.co.fitapet.infra.client.fcm.request;

import com.google.firebase.messaging.Notification;

public interface NotificationRequest {
    Notification toNotification();
    String title();
    String content();
    String imageUrl();
}
