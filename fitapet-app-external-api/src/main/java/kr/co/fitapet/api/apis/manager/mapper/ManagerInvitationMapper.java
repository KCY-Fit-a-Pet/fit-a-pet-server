package kr.co.fitapet.api.apis.manager.mapper;

import kr.co.fitapet.api.apis.manager.dto.InviteMemberInfoRes;
import kr.co.fitapet.common.annotation.Mapper;
import kr.co.fitapet.domain.domains.invitation.domain.ManagerInvitation;
import kr.co.fitapet.domain.domains.invitation.service.ManagerInvitationService;
import kr.co.fitapet.domain.domains.manager.service.ManagerSaveService;
import kr.co.fitapet.domain.domains.manager.service.ManagerSearchService;
import kr.co.fitapet.domain.domains.manager.type.ManageType;
import kr.co.fitapet.domain.domains.member.domain.Member;
import kr.co.fitapet.domain.domains.member.dto.MemberInfo;
import kr.co.fitapet.domain.domains.member.exception.AccountErrorCode;
import kr.co.fitapet.domain.domains.member.exception.AccountErrorException;
import kr.co.fitapet.domain.domains.member.service.MemberSearchService;
import kr.co.fitapet.domain.domains.pet.domain.Pet;
import kr.co.fitapet.domain.domains.pet.service.PetSearchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Mapper
@RequiredArgsConstructor
public class ManagerInvitationMapper {
    private final MemberSearchService memberSearchService;
    private final ManagerSearchService managerSearchService;
    private final ManagerSaveService managerSaveService;
    private final PetSearchService petSearchService;
    private final ManagerInvitationService managerInvitationService;

    @Transactional
    public void invite(Long managerId, Long toId, Long petId) {
        if (!memberSearchService.isExistById(toId))
            throw new AccountErrorException(AccountErrorCode.NOT_FOUND_MEMBER_ERROR);
        if (managerSearchService.isManager(toId, petId))
            throw new AccountErrorException(AccountErrorCode.ALREADY_MANAGER_ERROR);
        if (managerInvitationService.isExistsAndNotExpired(petId, toId))
            throw new AccountErrorException(AccountErrorCode.ALREADY_INVITED_ERROR);

        managerInvitationService.save(ManagerInvitation.of(
                memberSearchService.findById(managerId),
                memberSearchService.findById(toId),
                LocalDateTime.now().plusDays(1),
                petSearchService.findPetById(petId)
        ));
    }

    @Transactional(readOnly = true)
    public List<InviteMemberInfoRes.FromAspect> findInvitedMembers(Long requesterId, Long petId) {
        List<ManagerInvitation> invitations = managerInvitationService.findAllByPetIdNotExpiredAndNotAccepted(petId);

        List<Member> to = invitations.stream().map(ManagerInvitation::getTo).toList();
        List<MemberInfo> memberInfos = memberSearchService.findMemberInfos(to.stream().map(Member::getId).toList(), requesterId);
        Map<Long, MemberInfo> memberInfoMap = memberInfos.stream().collect(Collectors.toMap(MemberInfo::id, Function.identity()));

        return invitations.stream()
                    .map(invitation -> InviteMemberInfoRes.FromAspect.valueOf(invitation, memberInfoMap.get(invitation.getTo().getId())))
                    .toList();
    }

    @Transactional
    public void addManager(Long memberId, Pet pet, Long invitationId) {
        Member member = memberSearchService.findById(memberId);

        managerSaveService.mappingMemberAndPet(member, pet, ManageType.MANAGER);

        managerInvitationService.delete(managerInvitationService.findById(invitationId));
    }

    public void cancel(Long invitationId) {
        ManagerInvitation invitation = managerInvitationService.findById(invitationId);
        managerInvitationService.delete(invitation);
    }
}
