package com.kcy.fitapet.domain.log.service;

import com.kcy.fitapet.domain.log.dao.CareLogQueryRepository;
import com.kcy.fitapet.domain.log.dao.CareLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CareLogSearchService {
    private final CareLogRepository careLogRepository;
    private final CareLogQueryRepository careLogQueryRepository;

    @Transactional(readOnly = true)
    public boolean existsByCareIdOnLogDate(Long careId, LocalDateTime date) {
        return careLogQueryRepository.existsByCareDateIdAndLogDate(careId, date);
    }
}
