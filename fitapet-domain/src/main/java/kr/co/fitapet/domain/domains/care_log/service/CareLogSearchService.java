package kr.co.fitapet.domain.domains.care_log.service;

import kr.co.fitapet.common.annotation.DomainService;
import kr.co.fitapet.common.execption.GlobalErrorException;
import kr.co.fitapet.domain.domains.care_log.domain.CareLog;
import kr.co.fitapet.domain.domains.care_log.exception.CareLogErrorCode;
import kr.co.fitapet.domain.domains.care_log.repository.CareLogQueryRepository;
import kr.co.fitapet.domain.domains.care_log.repository.CareLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@DomainService
@RequiredArgsConstructor
public class CareLogSearchService {
    private final CareLogRepository careLogRepository;
    private final CareLogQueryRepository careLogQueryRepository;

    @Transactional(readOnly = true)
    public boolean existsByCareDateIdOnLogDate(Long careDateId, LocalDateTime date) {
        return careLogQueryRepository.existsByCareDateIdAndLogDate(careDateId, date);
    }

    @Transactional(readOnly = true)
    public CareLog findByCareDateIdOnLogDate(Long careDateId, LocalDateTime date) {
        return careLogQueryRepository.findByCareDateIdAndLogDate(careDateId, date).orElseThrow(
                () -> new GlobalErrorException(CareLogErrorCode.NOT_FOUND_CARE_LOG)
        );
    }
}
