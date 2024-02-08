package kr.co.fitapet.api.common.security.authentication;

import kr.co.fitapet.domain.domains.member.repository.MemberRepository;
import kr.co.fitapet.domain.domains.member.service.MemberSearchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserDetailServiceImpl implements UserDetailsService {
    private final MemberRepository userRepository;
//    private final MemberSearchService memberSearchService;

    @Override
    @Cacheable(value = "securityUser", key = "#userId", unless = "#result == null", cacheManager = "securityUserCacheManager")
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        log.debug("loadUserByUsername userId : {}", userId);
        return userRepository.findById(Long.parseLong(userId))
                .map(CustomUserDetails::of)
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));
//        try {
//            return CustomUserDetails.of(memberSearchService.findById(Long.parseLong(userId)));
//        } catch (Exception e) {
//            throw new UsernameNotFoundException("사용자를 찾을 수 없습니다.");
//        }
    }
}