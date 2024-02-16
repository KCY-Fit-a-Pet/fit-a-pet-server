package kr.co.fitapet.api.apis.manager.mapper;

import kr.co.fitapet.common.annotation.Mapper;
import kr.co.fitapet.domain.common.redis.manager.ManagerInvitationService;
import kr.co.fitapet.domain.domains.manager.service.ManagerSearchService;
import kr.co.fitapet.domain.domains.member.exception.AccountErrorCode;
import kr.co.fitapet.domain.domains.member.exception.AccountErrorException;
import kr.co.fitapet.domain.domains.member.service.MemberSearchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Mapper
@RequiredArgsConstructor
public class ManagerInvitationMapper {
    private final MemberSearchService memberSearchService;
    private final ManagerSearchService managerSearchService;
    private final ManagerInvitationService managerInvitationService;

    @Transactional
    public void invite(Long petId, Long invitedId) {
        log.info("invite manager in mapper. petId : {}, invitedId : {}", petId, invitedId);
        if (!memberSearchService.isExistById(invitedId))
            throw new AccountErrorException(AccountErrorCode.NOT_FOUND_MEMBER_ERROR);
        if (managerSearchService.isManager(invitedId, petId))
            throw new AccountErrorException(AccountErrorCode.ALREADY_MANAGER_ERROR);
        log.info("invite manager in mapper. petId : {}, invitedId : {}", petId, invitedId);
        managerInvitationService.save(invitedId, petId);
    }
}
