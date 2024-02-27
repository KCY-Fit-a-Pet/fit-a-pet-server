package kr.co.fitapet.api.common.event.notification;

import kr.co.fitapet.domain.domains.device.domain.DeviceToken;
import kr.co.fitapet.domain.domains.device.service.DeviceTokenSearchService;
import kr.co.fitapet.domain.domains.notification.domain.Notification;
import kr.co.fitapet.domain.domains.notification.service.NotificationSaveService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class NoticeEventHandler {
    private final ApplicationEventPublisher eventPublisher;
    private final DeviceTokenSearchService deviceTokenSearchService;
    private final NotificationSaveService notificationSaveService;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT, fallbackExecution = true)
    public void handleNoticeEvent(NoticeEvent event) {
        log.info("handleNoticeEvent: {}", event);
        // notice 저장
        // notice 전송할 대상 조회 (deviceToken 리스트)
        // deviceToken 리스트 전체 NotificationEvent 등록
    }

    /**
     * 공지사항 이벤트{@link AnnouncementEvent} 핸들러입니다.
     * <br/>
     * 공지사항 이벤트를 받아서 공지사항을 저장하고, 해당 공지사항을 전송할 대상을 조회하여 {@link Notification}를 등록합니다.
     * <br/>
     * {@link TransactionalEventListener}를 통해 이벤트를 발행하는 트랜잭션 커밋 이후에 핸들러가 실행됩니다.
     * <br/>
     * 이벤트를 발행한 트랜잭션과 별개의 트랜잭션으로 실행됩니다.
     *
     * @param event {@link AnnouncementEvent}
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT, fallbackExecution = true)
    public void handleAnnouncementEvent(AnnouncementEvent event) {
        log.info("handleAnnouncementEvent: {}", event);
        List<DeviceToken> deviceTokens = deviceTokenSearchService.findAll();

        Set<Notification> notifications = new HashSet<>();
        deviceTokens.forEach(deviceToken -> {
            String token = deviceToken.getDeviceToken();
            Long toId = deviceToken.getMember().getId();

            notifications.add(event.toEntity(toId));
        });
        notificationSaveService.saveAll(notifications);
    }
}
