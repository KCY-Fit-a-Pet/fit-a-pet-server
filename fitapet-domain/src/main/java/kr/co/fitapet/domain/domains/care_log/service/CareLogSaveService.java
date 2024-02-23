package kr.co.fitapet.domain.domains.care_log.service;

import kr.co.fitapet.common.annotation.DomainService;
import kr.co.fitapet.domain.domains.care_log.domain.CareLog;
import kr.co.fitapet.domain.domains.care_log.repository.CareLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@DomainService
@RequiredArgsConstructor
public class CareLogSaveService {
    private final CareLogRepository careLogRepository;

    @Transactional
    public CareLog save(CareLog careLog) {
        return careLogRepository.save(careLog);
    }

    @Transactional
    public void delete(CareLog careLog) {
        careLogRepository.delete(careLog);
    }
}
