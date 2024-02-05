package kr.co.fitapet.domain.domains.care_log.service;

import com.kcy.fitapet.domain.log.dao.CareLogRepository;
import com.kcy.fitapet.domain.log.domain.CareLog;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CareLogUpdateService {
    private final CareLogRepository careLogRepository;

    public CareLog save(CareLog careLog) {
        return careLogRepository.save(careLog);
    }
}
