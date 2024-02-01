package com.kcy.fitapet.domain.model;

import com.kcy.fitapet.domain.member.domain.Member;
import com.kcy.fitapet.global.common.security.authentication.CustomUserDetails;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class AuthorAwareAudit implements AuditorAware<Member> {
    private final EntityManager em;

    @Override
    public Optional<Member> getCurrentAuditor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || authentication.getPrincipal().equals("anonymousUser")) {
            return Optional.empty();
        }

        Long userId = ((CustomUserDetails) authentication.getPrincipal()).getUserId();

        return Optional.ofNullable(em.getReference(Member.class, userId));
    }
}
