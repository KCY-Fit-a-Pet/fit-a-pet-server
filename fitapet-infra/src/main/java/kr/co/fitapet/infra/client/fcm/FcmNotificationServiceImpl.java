package kr.co.fitapet.infra.client.fcm;

import com.google.api.core.ApiFuture;
import com.google.firebase.messaging.*;
import kr.co.fitapet.infra.client.fcm.request.NotificationMulticastRequest;
import kr.co.fitapet.infra.client.fcm.request.NotificationRequest;
import kr.co.fitapet.infra.client.fcm.request.NotificationSingleRequest;
import kr.co.fitapet.infra.client.fcm.request.NotificationTopicRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class FcmNotificationServiceImpl implements NotificationService {
    private final FirebaseMessaging firebaseMessaging;

    @Override
    public void sendMessage(NotificationSingleRequest request) throws FirebaseMessagingException {
        Message message = request.buildSendMessage().setApnsConfig(getApnsConfigToTopic(request)).build();

        ApiFuture<String> response = firebaseMessaging.sendAsync(message);
        log.info("Successfully sent message: " + response);
    }

    @Override
    public void sendMessages(NotificationMulticastRequest request) throws FirebaseMessagingException {
        MulticastMessage messages = request.buildSendMessage().setApnsConfig(getApnsConfigToTopic(request)).build();

        ApiFuture<BatchResponse> response = firebaseMessaging.sendEachForMulticastAsync(messages);
        log.info("Successfully sent message: " + response);
    }

    @Override
    public void sendMessagesToTopic(NotificationTopicRequest request) throws FirebaseMessagingException {
        Message message = request.buildSendMessage().setApnsConfig(getApnsConfigToTopic(request)).build();

        ApiFuture<String> response = firebaseMessaging.sendAsync(message);
        log.info("Successfully sent message: " + response);
    }

    @Override
    public void subScribe(String topic, List<String> memberTokens) throws FirebaseMessagingException {
        firebaseMessaging.subscribeToTopicAsync(memberTokens, topic);
    }

    @Override
    public void unSubScribe(String topic, List<String> memberTokens) throws FirebaseMessagingException {
        firebaseMessaging.unsubscribeFromTopicAsync(memberTokens, topic);
    }

    private ApnsConfig getApnsConfigToTopic(NotificationRequest request) {
        return ApnsConfig.builder()
                .setAps(Aps.builder()
                        .setAlert(
                                ApsAlert.builder()
                                    .setTitle(request.title())
                                    .setBody(request.content())
                                    .setLaunchImage(request.imageUrl())
                                .build())
                        .setSound("default")
                        .build())
                .build();
    }
}
