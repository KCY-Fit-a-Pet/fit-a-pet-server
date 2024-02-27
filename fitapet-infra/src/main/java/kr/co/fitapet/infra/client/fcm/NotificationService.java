package kr.co.fitapet.infra.client.fcm;

import com.google.firebase.messaging.FirebaseMessagingException;
import kr.co.fitapet.infra.client.fcm.request.NotificationMulticastRequest;
import kr.co.fitapet.infra.client.fcm.request.NotificationSingleRequest;
import kr.co.fitapet.infra.client.fcm.request.NotificationTopicRequest;

import java.util.List;

public interface NotificationService {
    void sendMessage(NotificationSingleRequest request);
    void sendMessages(NotificationMulticastRequest request);
    void sendMessagesToTopic(NotificationTopicRequest request);
    void subScribe(String topic, List<String> memberTokens);
    void unSubScribe(String topic, List<String> memberTokens);
}
