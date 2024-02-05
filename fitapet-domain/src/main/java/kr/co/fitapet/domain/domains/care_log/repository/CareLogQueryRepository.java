package kr.co.fitapet.domain.domains.care_log.repository;

import java.time.LocalDateTime;

public interface CareLogQueryRepository {
    boolean existsByCareDateIdAndLogDate(Long careDateId, LocalDateTime logDate);
}
