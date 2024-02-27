package kr.co.fitapet.domain.domains.notification.service;

import kr.co.fitapet.common.annotation.DomainService;
import kr.co.fitapet.domain.domains.notification.domain.Notification;
import kr.co.fitapet.domain.domains.notification.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;

@DomainService
@RequiredArgsConstructor
public class NotificationSaveService {
    private final NotificationRepository notificationRepository;

    public void save(Notification notification) {
        notificationRepository.save(notification);
    }

    public void saveAll(Iterable<Notification> notifications) {
        notificationRepository.saveAll(notifications);
    }
}
