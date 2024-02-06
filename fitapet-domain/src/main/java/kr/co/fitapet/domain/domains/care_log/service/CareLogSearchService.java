package kr.co.fitapet.domain.domains.care_log.service;

import kr.co.fitapet.domain.domains.care_log.repository.CareLogQueryRepository;
import kr.co.fitapet.domain.domains.care_log.repository.CareLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CareLogSearchService {
    private final CareLogRepository careLogRepository;
    private final CareLogQueryRepository careLogQueryRepository;

    @Transactional(readOnly = true)
    public boolean existsByCareDateIdOnLogDate(Long careDateId, LocalDateTime date) {
        return careLogQueryRepository.existsByCareDateIdAndLogDate(careDateId, date);
    }
}
