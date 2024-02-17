package kr.co.fitapet.api.apis.pet.mapper;

import kr.co.fitapet.common.annotation.Mapper;
import kr.co.fitapet.domain.domains.manager.domain.Manager;
import kr.co.fitapet.domain.domains.manager.service.ManagerSaveService;
import kr.co.fitapet.domain.domains.manager.service.ManagerSearchService;
import kr.co.fitapet.domain.domains.member.service.MemberSearchService;
import kr.co.fitapet.domain.domains.manager.type.ManageType;
import kr.co.fitapet.domain.domains.pet.domain.Pet;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Mapper
@Slf4j
@RequiredArgsConstructor
public class PetManagerMapper {
    private final MemberSearchService memberSearchService;
    private final ManagerSearchService managerSearchService;
    private final ManagerSaveService managerSaveService;

    @Transactional
    public void mappingMemberAndPet(Long memberId, Pet pet) {
        managerSaveService.mappingMemberAndPet(memberSearchService.findById(memberId), pet, ManageType.MASTER);
    }

    @Transactional(readOnly = true)
    public List<Pet> findAllPetByMemberId(Long memberId) {
        return managerSearchService.findAllByMemberId(memberId).stream().map(Manager::getPet).toList();
    }

    @Transactional(readOnly = true)
    public boolean isManagerAll(Long memberId, List<Long> petIds) {
        return managerSearchService.isManagerAll(memberId, petIds);
    }

    @Transactional(readOnly = true)
    public List<Pet> findAllManagerByMemberId(Long memberId) {
        return managerSearchService.findAllByMemberId(memberId)
                .stream().map(Manager::getPet).toList();
    }
}
