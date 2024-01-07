package com.kcy.fitapet.global.common.security.authorization;

import com.kcy.fitapet.domain.member.dao.ManagerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component("careAuthorizeManager")
@RequiredArgsConstructor
@Slf4j
public class CareAuthorizeManager {
    private final ManagerRepository managerRepository;

    public boolean isManager(Long memberId, Long petId) {
        return managerRepository.existsByMember_IdAndPet_Id(memberId, petId);
    }
}
