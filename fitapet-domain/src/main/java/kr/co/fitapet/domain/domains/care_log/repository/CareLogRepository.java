package kr.co.fitapet.domain.domains.care_log.repository;

import com.kcy.fitapet.domain.log.domain.CareLog;
import com.kcy.fitapet.domain.log.domain.CareLogId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;

public interface CareLogRepository extends JpaRepository<CareLog, CareLogId> {
    boolean existsByCareDate_IdAndLogDate(Long careId, LocalDate today);
}
