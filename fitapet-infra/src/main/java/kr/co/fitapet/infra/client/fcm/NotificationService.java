package kr.co.fitapet.infra.client.fcm;

import com.google.firebase.messaging.FirebaseMessagingException;

import java.util.List;

public interface NotificationService {
    void sendMessage(NotificationRequest request) throws FirebaseMessagingException;
    void sendMessages(NotificationRequest request) throws FirebaseMessagingException;
    void sendMessagesToTopic(NotificationRequest request) throws FirebaseMessagingException;
    void subScribe(String topic, List<String> memberTokens) throws FirebaseMessagingException;
    void unSubScribe(String topic, List<String> memberTokens) throws FirebaseMessagingException;
}
