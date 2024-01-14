package com.kcy.fitapet.domain.log.service;

import com.kcy.fitapet.domain.log.dao.CareLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class CareLogSearchService {
    private final CareLogRepository careLogRepository;

    @Transactional(readOnly = true)
    public boolean existsByCareIdOnToday(Long careId, LocalDate today) {
        return careLogRepository.existsByCareDate_IdAndLogDate(careId, today);
    }
}
