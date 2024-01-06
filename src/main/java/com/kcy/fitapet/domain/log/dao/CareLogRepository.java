package com.kcy.fitapet.domain.log.dao;

import com.kcy.fitapet.domain.log.domain.CareLog;
import com.kcy.fitapet.domain.log.domain.CareLogId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CareLogRepository extends JpaRepository<CareLog, CareLogId> {
}
