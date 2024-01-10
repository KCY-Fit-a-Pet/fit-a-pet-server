package com.kcy.fitapet.global.common.security.authorization;

import com.kcy.fitapet.domain.member.dao.ManagerRepository;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component("managerAuthorize")
@RequiredArgsConstructor
@Slf4j
public class ManagerAuthorize {
    private final ManagerRepository managerRepository;
    private final EntityManager em;

    public boolean isManager(Long memberId, Long petId) {
        return managerRepository.existsByMember_IdAndPet_Id(memberId, petId);
    }
}
