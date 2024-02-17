package kr.co.fitapet.api.common.security.authorization;

import kr.co.fitapet.domain.common.redis.manager.ManagerInvitationService;
import kr.co.fitapet.domain.domains.manager.service.ManagerSearchService;
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
        Long masterId = managerSearchService.findMasterIdByPetId(petId);
        log.info("masterId: {}", masterId);
        return memberId.equals(masterId);
    }

    public boolean isInvitedMember(Long memberId, Long petId) {
        return !managerInvitationService.expired(memberId, petId);
    }
}
