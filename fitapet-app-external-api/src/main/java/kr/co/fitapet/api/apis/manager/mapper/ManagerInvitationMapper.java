package kr.co.fitapet.api.apis.manager.mapper;

import kr.co.fitapet.api.apis.manager.dto.InviteMemberInfoRes;
import kr.co.fitapet.common.annotation.Mapper;
import kr.co.fitapet.domain.common.redis.manager.InvitationDto;
import kr.co.fitapet.domain.common.redis.manager.ManagerInvitationService;
import kr.co.fitapet.domain.domains.manager.service.ManagerSearchService;
import kr.co.fitapet.domain.domains.member.domain.Member;
import kr.co.fitapet.domain.domains.member.exception.AccountErrorCode;
import kr.co.fitapet.domain.domains.member.exception.AccountErrorException;
import kr.co.fitapet.domain.domains.member.service.MemberSearchService;
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
    public List<InviteMemberInfoRes> findInvitedMembers(Long petId) {
        List<InvitationDto> invitationDtos = managerInvitationService.findAll(petId);
        List<Member> members = memberSearchService.findByIds(invitationDtos.stream().map(InvitationDto::inviteId).toList());
        return members.stream().map(member -> {
            InvitationDto invitationDto = invitationDtos.stream().filter(dto -> dto.inviteId().equals(member.getId())).findFirst().orElseThrow();
            return InviteMemberInfoRes.valueOf(member, invitationDto.ttl(), invitationDto.expired());
        }).toList();
    }
}
