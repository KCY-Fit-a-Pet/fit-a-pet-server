package kr.co.fitapet.api.apis.manager.usecase;

import kr.co.fitapet.api.apis.manager.dto.InviteMemberInfoRes;
import kr.co.fitapet.api.apis.manager.mapper.ManagerInvitationMapper;
import kr.co.fitapet.common.annotation.UseCase;
import kr.co.fitapet.domain.domains.manager.domain.Manager;
import kr.co.fitapet.domain.domains.manager.dto.ManagerInfoRes;
import kr.co.fitapet.domain.domains.manager.service.ManagerDeleteService;
import kr.co.fitapet.domain.domains.manager.service.ManagerSaveService;
import kr.co.fitapet.domain.domains.manager.service.ManagerSearchService;
import kr.co.fitapet.domain.domains.pet.domain.Pet;
import kr.co.fitapet.domain.domains.pet.service.PetSearchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@UseCase
@RequiredArgsConstructor
public class ManagerUseCase {
    private final ManagerSearchService managerSearchService;
    private final ManagerDeleteService managerDeleteService;

    private final PetSearchService petSearchService;

    private final ManagerInvitationMapper managerInvitationMapper;

    @Transactional(readOnly = true)
    public List<ManagerInfoRes> findManagers(Long memberId, Long petId) {
        return managerSearchService.findAllByPetId(petId, memberId);
    }

    @Transactional(readOnly = true)
    public void invite(Long managerId, Long toId, Long petId) {
        managerInvitationMapper.invite(managerId, toId, petId);
    }

    @Transactional(readOnly = true)
    public List<InviteMemberInfoRes.FromAspect> findInvitedMembers(Long requesterId, Long petId) {
        return managerInvitationMapper.findInvitedMembers(requesterId, petId);
    }

    @Transactional
    public void agreeInvite(Long memberId, Long petId, Long invitationId) {
        Pet pet = petSearchService.findPetById(petId);
        managerInvitationMapper.addManager(memberId, pet, invitationId);
    }

    public void cancelInvite(Long invitationId) {
        managerInvitationMapper.cancel(invitationId);
    }

    @Transactional
    @CacheEvict(value = "master", key = "#masterId + '@' + #petId", cacheManager = "managerCacheManager")
    public void delegateMaster(Long masterId, Long managerId, Long petId) {
        Manager master = managerSearchService.findByMemberIdAndPetId(masterId, petId);
        Manager manager = managerSearchService.findByMemberIdAndPetId(managerId, petId);

        master.delegateMaster(manager);
    }

    @Transactional
    public void expelManager(Long targetId, Long petId) {
        Manager manager = managerSearchService.findByMemberIdAndPetId(targetId, petId);
        managerDeleteService.deleteManager(manager);
    }
}
