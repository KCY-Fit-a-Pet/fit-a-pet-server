package kr.co.fitapet.domain.domains.manager.service;

import kr.co.fitapet.common.annotation.DomainService;
import kr.co.fitapet.domain.domains.manager.domain.Manager;
import kr.co.fitapet.domain.domains.manager.repository.ManagerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@DomainService
@RequiredArgsConstructor
public class ManagerDeleteService {
    private final ManagerRepository managerRepository;

    @Transactional
    public void deleteManager(Manager manager) {
        managerRepository.delete(manager);
    }
}
