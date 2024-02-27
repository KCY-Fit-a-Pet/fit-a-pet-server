package kr.co.fitapet.api.common.event.notification;

import kr.co.fitapet.domain.domains.device.domain.DeviceToken;
import kr.co.fitapet.domain.domains.device.exception.DeviceTokenErrorCode;
import kr.co.fitapet.domain.domains.device.exception.DeviceTokenErrorException;
import kr.co.fitapet.domain.domains.device.service.DeviceTokenSearchService;
import kr.co.fitapet.domain.domains.notification.domain.Notification;
import kr.co.fitapet.domain.domains.notification.service.NotificationSaveService;
import kr.co.fitapet.domain.domains.notification.type.NotificationType;
import kr.co.fitapet.infra.client.fcm.NotificationDataKey;
import kr.co.fitapet.infra.common.event.NotificationSingleEvent;
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

    /**
     * 일반적인 push notification 이벤트{@link NoticeEvent} 핸들러입니다.
     * <br/>
     * 알림 이벤트를 받아서 알림을 저장하고, 해당 알림을 전송할 대상을 조회하여 {@link Notification}를 등록합니다.
     * <br/>
     * {@link TransactionalEventListener}를 통해 이벤트를 발행하는 트랜잭션 커밋 이후에 핸들러가 실행됩니다.
     * <br/>
     * 이벤트를 발행한 트랜잭션과 별개의 트랜잭션으로 실행됩니다.
     *
     * @param event {@link NoticeEvent}
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT, fallbackExecution = true)
    public void handleNoticeEvent(NoticeEvent event) {
        log.info("handleNoticeEvent: {}", event);
        Notification notification = event.toEntity();
        notificationSaveService.save(notification);

        List<DeviceToken> deviceTokens = deviceTokenSearchService.findDeviceTokensByMemberId(event.memberId());
        if (deviceTokens.isEmpty()) {
            log.warn("No device tokens found for member: {}", event.memberId());
            throw new DeviceTokenErrorException(DeviceTokenErrorCode.NOT_FOUND_DEVICE_TOKEN_ERROR);
        }

        deviceTokens.forEach(deviceToken -> {
            eventPublisher.publishEvent(
                    NotificationSingleEvent.builder()
                            .deviceToken(deviceToken.getDeviceToken())
                            .title(event.title())
                            .content(event.getFormattedContent())
                            .imageUrl(event.imageUrl())
                            .data(event.data())
                            .build()
            );
        });
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
            Map<NotificationDataKey, String> data = Map.of(
                    NotificationDataKey.NOTICE_TYPE, NotificationType.ANNOUNCEMENT.name(),
                    NotificationDataKey.TO_ID, deviceToken.getMember().getId().toString(),
                    NotificationDataKey.TO_NAME, deviceToken.getMember().getName()
            );

            eventPublisher.publishEvent(
                    NotificationSingleEvent.builder()
                            .deviceToken(token)
                            .title(event.title())
                            .content(event.content())
                            .imageUrl(event.imageUrl())
                            .data(data)
                            .build()
            );
        });
        notificationSaveService.saveAll(notifications);
    }
}
