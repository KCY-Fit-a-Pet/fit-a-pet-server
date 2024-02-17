package kr.co.fitapet.api.apis.manager.usecase;

import kr.co.fitapet.api.apis.manager.dto.InviteMemberInfoRes;
import kr.co.fitapet.api.apis.manager.mapper.ManagerInvitationMapper;
import kr.co.fitapet.common.annotation.UseCase;
import kr.co.fitapet.domain.domains.manager.dto.ManagerInfoRes;
import kr.co.fitapet.domain.domains.manager.service.ManagerSearchService;
import kr.co.fitapet.domain.domains.pet.domain.Pet;
import kr.co.fitapet.domain.domains.pet.service.PetSearchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@UseCase
@RequiredArgsConstructor
public class ManagerUseCase {
    private final ManagerSearchService managerSearchService;
    private final PetSearchService petSearchService;
    private final ManagerInvitationMapper managerInvitationMapper;

    @Transactional(readOnly = true)
    public List<ManagerInfoRes> findManagers(Long petId, Long memberId) {
        return managerSearchService.findAllManagerByPetId(petId, memberId);
    }

    @Transactional(readOnly = true)
    public void invite(Long petId, Long invitedId) {
        managerInvitationMapper.invite(petId, invitedId);
    }

    @Transactional(readOnly = true)
    public List<InviteMemberInfoRes> findInvitedMembers(Long petId) {
        return managerInvitationMapper.findInvitedMembers(petId);
    }

    @Transactional
    public void agreeInvite(Long petId, Long memberId) {
        Pet pet = petSearchService.findPetById(petId);
        managerInvitationMapper.addManager(memberId, pet);
    }

    public void cancelInvite(Long petId, Long memberId) {
        managerInvitationMapper.cancel(petId, memberId);
    }
}
