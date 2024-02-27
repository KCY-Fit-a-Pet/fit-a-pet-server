package kr.co.fitapet.infra.common.event;

import com.google.firebase.FirebaseException;
import kr.co.fitapet.infra.client.fcm.NotificationEvent;
import kr.co.fitapet.infra.client.fcm.NotificationRequest;
import kr.co.fitapet.infra.client.fcm.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
@Slf4j
public class FcmNotificationEventHandler {
    private final NotificationService notificationService;

    @TransactionalEventListener
    public void handleTokensEvent(NotificationEvent event) {
        log.info("handleTokensEvent: {}", event);
        NotificationRequest request = NotificationRequest.fromEvent(event);

        try {
            if (event.isTopic()) {
                notificationService.sendMessagesToTopic(request);
            } else if (event.isMulticast()) {
                notificationService.sendMessages(request);
            } else {
                notificationService.sendMessage(request);
            }
        } catch (FirebaseException e) {
            e.printStackTrace();
        }

        log.info("Successfully sent FCM message");
    }
}
