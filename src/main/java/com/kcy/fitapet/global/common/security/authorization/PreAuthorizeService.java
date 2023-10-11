package com.kcy.fitapet.global.common.security.authorization;

import com.kcy.fitapet.domain.member.service.module.MemberSearchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service("preAuthorizeService")
@Slf4j
@RequiredArgsConstructor
public class PreAuthorizeService {
    private final MemberSearchService memberSearchService;

    public boolean test(Long requestMemberId) {
        log.info("is start pre authorization check");

        log.info("user role type : {}", requestMemberId);
        return true;
    }
}
