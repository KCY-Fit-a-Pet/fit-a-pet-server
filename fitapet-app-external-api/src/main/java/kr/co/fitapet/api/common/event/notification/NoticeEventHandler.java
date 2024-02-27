package kr.co.fitapet.api.common.event.notification;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Component
@RequiredArgsConstructor
public class NoticeEventHandler {
    private final ApplicationEventPublisher eventPublisher;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT, fallbackExecution = true)
    public void handleNoticeEventToManager(NoticeEvent event) {
        log.info("handleNoticeEvent: {}", event);
        // notice 저장
        // notice 전송할 대상 조회 (deviceToken 리스트)
        // deviceToken 리스트 전체 NotificationEvent 등록
    }
}
