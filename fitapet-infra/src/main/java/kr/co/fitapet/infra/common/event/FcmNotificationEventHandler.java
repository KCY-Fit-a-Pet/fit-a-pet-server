package kr.co.fitapet.infra.common.event;

import com.google.firebase.FirebaseException;
import kr.co.fitapet.infra.client.fcm.request.NotificationMulticastRequest;
import kr.co.fitapet.infra.client.fcm.request.NotificationSingleRequest;
import kr.co.fitapet.infra.client.fcm.NotificationService;
import kr.co.fitapet.infra.client.fcm.request.NotificationTopicRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
@Slf4j
public class FcmNotificationEventHandler {
    private final NotificationService notificationService;

    /**
     * 단일 토큰에게 FCM 메시지를 전송합니다.
     * <br/>
     * {@link NotificationSingleEvent}를 받아서 FCM 메시지를 전송합니다.
     * <br/>
     * {@link TransactionalEventListener}를 통해 이벤트를 발행하는 트랜잭션 커밋 이후에 핸들러가 실행됩니다.
     * <br/>
     * 해당 이벤트는 트랜잭션 내에서 실행되지 않습니다.
     * @param event {@link NotificationSingleEvent}
     */
    @TransactionalEventListener
    public void handleTokenEvent(NotificationSingleEvent event) {
        log.info("handleTokensEvent: {}", event);
        NotificationSingleRequest request = NotificationSingleRequest.fromEvent(event);

        notificationService.sendMessage(request);

        log.info("Successfully sent FCM message");
    }

    /**
     * 다중 토큰에게 FCM 메시지를 전송합니다.
     * <br/>
     * {@link NotificationMulticastEvent}를 받아서 FCM 메시지를 전송합니다.
     * <br/>
     * {@link TransactionalEventListener}를 통해 이벤트를 발행하는 트랜잭션 커밋 이후에 핸들러가 실행됩니다.
     * <br/>
     * 해당 이벤트는 트랜잭션 내에서 실행되지 않습니다.
     * @param event {@link NotificationMulticastEvent}
     */
    @TransactionalEventListener
    public void handleTokensEvent(NotificationMulticastEvent event) {
        log.info("handleTokensEvent: {}", event);
        NotificationMulticastRequest request = NotificationMulticastRequest.fromEvent(event);

        notificationService.sendMessages(request);

        log.info("Successfully sent FCM message");
    }

    /**
     * 토픽에게 FCM 메시지를 전송합니다.
     * <br/>
     * {@link NotificationTopicEvent}를 받아서 FCM 메시지를 전송합니다.
     * <br/>
     * {@link TransactionalEventListener}를 통해 이벤트를 발행하는 트랜잭션 커밋 이후에 핸들러가 실행됩니다.
     * <br/>
     * 해당 이벤트는 트랜잭션 내에서 실행되지 않습니다.
     * @param event {@link NotificationTopicEvent}
     */
    @TransactionalEventListener
    public void handleTopicEvent(NotificationTopicEvent event) {
        log.info("handleTopicEvent: {}", event);
        NotificationTopicRequest request = NotificationTopicRequest.fromEvent(event);

        notificationService.sendMessagesToTopic(request);

        log.info("Successfully sent FCM message");
    }
}
