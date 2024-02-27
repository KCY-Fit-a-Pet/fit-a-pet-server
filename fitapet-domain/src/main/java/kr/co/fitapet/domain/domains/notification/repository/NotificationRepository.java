package kr.co.fitapet.domain.domains.notification.repository;

import kr.co.fitapet.domain.domains.notification.domain.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
}
