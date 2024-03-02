package kr.co.fitapet.api.apis.manager.mapper;

import kr.co.fitapet.api.apis.manager.dto.InviteMemberInfoRes;
import kr.co.fitapet.common.annotation.Mapper;
import kr.co.fitapet.domain.common.redis.manager.InvitationDto;
//import kr.co.fitapet.domain.domains.invitation.service.ManagerInvitationService;
import kr.co.fitapet.domain.common.redis.manager.ManagerInvitationService;
import kr.co.fitapet.domain.domains.manager.service.ManagerSaveService;
import kr.co.fitapet.domain.domains.manager.service.ManagerSearchService;
import kr.co.fitapet.domain.domains.manager.type.ManageType;
import kr.co.fitapet.domain.domains.member.domain.Member;
import kr.co.fitapet.domain.domains.member.dto.MemberInfo;
import kr.co.fitapet.domain.domains.member.exception.AccountErrorCode;
import kr.co.fitapet.domain.domains.member.exception.AccountErrorException;
import kr.co.fitapet.domain.domains.member.service.MemberSearchService;
import kr.co.fitapet.domain.domains.pet.domain.Pet;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Mapper
@RequiredArgsConstructor
public class ManagerInvitationMapper {
    private final MemberSearchService memberSearchService;
    private final ManagerSearchService managerSearchService;
    private final ManagerSaveService managerSaveService;
    private final ManagerInvitationService managerInvitationService;

    @Transactional(readOnly = true)
    public void invite(Long petId, Long invitedId) {
        if (!memberSearchService.isExistById(invitedId))
            throw new AccountErrorException(AccountErrorCode.NOT_FOUND_MEMBER_ERROR);
        if (managerSearchService.isManager(invitedId, petId))
            throw new AccountErrorException(AccountErrorCode.ALREADY_MANAGER_ERROR);
        managerInvitationService.save(invitedId, petId);
    }

    @Transactional(readOnly = true)
    public List<InviteMemberInfoRes> findInvitedMembers(Long petId, Long requesterId) {
        List<InvitationDto> invitationDtos = managerInvitationService.findAll(petId);
        List<MemberInfo> members = memberSearchService.findMemberInfos(invitationDtos.stream().map(InvitationDto::inviteId).toList(), requesterId);
        return members.stream().map(member -> {
            InvitationDto invitationDto = invitationDtos.stream().filter(dto -> dto.inviteId().equals(member.id())).findFirst().orElseThrow();
            return InviteMemberInfoRes.valueOf(member, invitationDto.ttl(), invitationDto.expired());
        }).toList();
    }

    @Transactional
    public void addManager(Long memberId, Pet pet) {
        Member member = memberSearchService.findById(memberId);
        managerSaveService.mappingMemberAndPet(member, pet, ManageType.MANAGER);
        managerInvitationService.delete(memberId, pet.getId());
    }

    public void cancel(Long petId, Long memberId) {
        managerInvitationService.delete(memberId, petId);
    }
}
