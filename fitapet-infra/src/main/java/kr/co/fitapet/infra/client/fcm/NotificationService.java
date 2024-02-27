package kr.co.fitapet.infra.client.fcm;

import com.google.firebase.messaging.FirebaseMessagingException;
import kr.co.fitapet.infra.client.fcm.request.NotificationMulticastRequest;
import kr.co.fitapet.infra.client.fcm.request.NotificationSingleRequest;
import kr.co.fitapet.infra.client.fcm.request.NotificationTopicRequest;

import java.util.List;

public interface NotificationService {
    void sendMessage(NotificationSingleRequest request) throws FirebaseMessagingException;
    void sendMessages(NotificationMulticastRequest request) throws FirebaseMessagingException;
    void sendMessagesToTopic(NotificationTopicRequest request) throws FirebaseMessagingException;
    void subScribe(String topic, List<String> memberTokens) throws FirebaseMessagingException;
    void unSubScribe(String topic, List<String> memberTokens) throws FirebaseMessagingException;
}
