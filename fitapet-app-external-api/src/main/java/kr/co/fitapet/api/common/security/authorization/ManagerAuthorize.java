package kr.co.fitapet.api.common.security.authorization;

import kr.co.fitapet.domain.common.redis.manager.ManagerInvitationService;
import kr.co.fitapet.domain.domains.manager.service.ManagerSearchService;
import kr.co.fitapet.domain.domains.member.service.MemberSearchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

@Component("managerAuthorize")
@RequiredArgsConstructor
@Slf4j
public class ManagerAuthorize {
    private final ManagerSearchService managerSearchService;
    private final ManagerInvitationService managerInvitationService;

    @Cacheable(value = "manager", key = "#memberId + '@' + #petId", unless = "#result == false", cacheManager = "managerCacheManager")
    public boolean isManager(Long memberId, Long petId) {
        return managerSearchService.isManager(memberId, petId);
    }

    @Cacheable(value = "master", key = "#memberId + '@' + #petId", unless = "#result == false", cacheManager = "managerCacheManager")
    public boolean isMaster(Long memberId, Long petId) {
        return memberId.equals(managerSearchService.findMasterIdByPetId(petId));
    }

    public boolean isInvitedMember(Long memberId, Long petId) {
        // TODO : Redis 조회 후 있으면 true -> 캐시 삭제
        log.info("expired : {}", managerInvitationService.expired(memberId, petId));

        return true;
    }
}
