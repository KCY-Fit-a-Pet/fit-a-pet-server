package kr.co.fitapet.infra.client.fcm;

import com.google.firebase.messaging.FirebaseMessagingException;

public interface NotificationService {
    void sendNotification(NotificationRequest request) throws FirebaseMessagingException;
}
