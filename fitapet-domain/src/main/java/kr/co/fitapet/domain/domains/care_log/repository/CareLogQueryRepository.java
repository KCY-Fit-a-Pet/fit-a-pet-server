package kr.co.fitapet.domain.domains.care_log.repository;

import kr.co.fitapet.domain.domains.care_log.domain.CareLog;

import java.time.LocalDateTime;
import java.util.Optional;

public interface CareLogQueryRepository {
    boolean existsByCareDateIdAndLogDate(Long careDateId, LocalDateTime logDate);
    Optional<CareLog> findByCareDateIdAndLogDate(Long careDateId, LocalDateTime logDate);
}
