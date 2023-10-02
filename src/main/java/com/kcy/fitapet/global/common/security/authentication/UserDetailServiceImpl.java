package com.kcy.fitapet.global.common.security.authentication;

import com.kcy.fitapet.domain.member.dao.MemberRepository;
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

    @Override
    @Cacheable(value = "securityUser", key = "#userId", unless = "#result == null")
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        log.debug("loadUserByUsername userId : {}", userId);
        return userRepository.findById(Long.parseLong(userId))
                .map(CustomUserDetails::of)
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));
    }
}