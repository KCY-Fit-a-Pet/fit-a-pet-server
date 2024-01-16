package com.kcy.fitapet.domain.log.dao;

import java.time.LocalDateTime;

public interface CareLogQueryRepository {
    boolean existsByCareDateIdAndLogDate(Long careDateId, LocalDateTime logDate);
}
