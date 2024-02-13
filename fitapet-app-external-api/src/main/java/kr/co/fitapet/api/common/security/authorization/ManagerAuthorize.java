package kr.co.fitapet.api.common.security.authorization;

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

    @Cacheable(value = "manager", key = "#memberId + '@' + #petId", unless = "#result == false", cacheManager = "managerCacheManager")
    public boolean isManager(Long memberId, Long petId) {
        return managerSearchService.isManager(memberId, petId);
    }
}
