package kr.co.fitapet.domain.domains.care_log.service;

import kr.co.fitapet.common.annotation.DomainService;
import kr.co.fitapet.domain.domains.care_log.domain.CareLog;
import kr.co.fitapet.domain.domains.care_log.repository.CareLogRepository;
import lombok.RequiredArgsConstructor;

@DomainService
@RequiredArgsConstructor
public class CareLogUpdateService {
    private final CareLogRepository careLogRepository;

    public CareLog save(CareLog careLog) {
        return careLogRepository.save(careLog);
    }
}
