package kr.co.fitapet.domain.domains.manager.service;

import kr.co.fitapet.common.annotation.UseCase;
import kr.co.fitapet.domain.domains.manager.domain.Manager;
import kr.co.fitapet.domain.domains.manager.repository.ManagerRepository;
import kr.co.fitapet.domain.domains.member.domain.Member;
import kr.co.fitapet.domain.domains.member.type.ManageType;
import kr.co.fitapet.domain.domains.pet.domain.Pet;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@UseCase
@RequiredArgsConstructor
public class ManagerSaveService {
    private final ManagerRepository managerRepository;

    @Transactional
    public void mappingMemberAndPet(Member member, Pet pet, ManageType manageType) {
        Manager manager = Manager.of(member, pet, false, manageType);
        managerRepository.save(manager);
    }
}

