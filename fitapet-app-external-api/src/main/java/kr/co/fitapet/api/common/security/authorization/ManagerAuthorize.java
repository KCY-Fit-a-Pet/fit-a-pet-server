package kr.co.fitapet.api.common.security.authorization;

import com.kcy.fitapet.domain.member.dao.ManagerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

@Component("managerAuthorize")
@RequiredArgsConstructor
@Slf4j
public class ManagerAuthorize {
    private final ManagerRepository managerRepository;

    @Cacheable(value = "manager", key = "#memberId + '@' + #petId", unless = "#result == false", cacheManager = "managerCacheManager")
    public boolean isManager(Long memberId, Long petId) {
        return managerRepository.existsByMember_IdAndPet_Id(memberId, petId);
    }
}
