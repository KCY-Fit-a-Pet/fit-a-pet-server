package kr.co.fitapet.domain.domains.manager.service;

import kr.co.fitapet.common.annotation.DomainService;
import kr.co.fitapet.domain.domains.manager.domain.Manager;
import kr.co.fitapet.domain.domains.manager.dto.ManagerInfoRes;
import kr.co.fitapet.domain.domains.manager.repository.ManagerRepository;
import kr.co.fitapet.domain.domains.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@DomainService
@RequiredArgsConstructor
public class ManagerSearchService {
    private final ManagerRepository managerRepository;

    @Transactional(readOnly = true)
    public List<Manager> findAllManagerByMemberId(Long memberId) {
        return managerRepository.findAllByMember_Id(memberId);
    }

    @Transactional(readOnly = true)
    public boolean isManager(Long memberId, Long petId) {
        return managerRepository.existsByMember_IdAndPet_Id(memberId, petId);
    }

    @Transactional(readOnly = true)
    public boolean isManagerAll(Long memberId, List<Long> petIds) {
        if (petIds.isEmpty()) {
            return true;
        }

        for (Long petId : petIds) {
            if (!isManager(memberId, petId)) {
                return false;
            }
        }
        return true;
    }

    @Transactional(readOnly = true)
    public Long findMasterIdByPetId(Long petId) {
        return managerRepository.findMasterIdByPetId(petId);
    }

    @Transactional(readOnly = true)
    public List<ManagerInfoRes> findAllManagerByPetId(Long petId, Long memberId) {
        return managerRepository.findAllManager(petId, memberId);
    }
}
