package com.kcy.fitapet.domain.oauth.service.module;

import com.kcy.fitapet.domain.member.domain.Member;
import com.kcy.fitapet.domain.oauth.dao.OauthJpaRepository;
import com.kcy.fitapet.domain.oauth.domain.OauthAccount;
import com.kcy.fitapet.domain.oauth.exception.OauthException;
import com.kcy.fitapet.domain.oauth.type.ProviderType;
import com.kcy.fitapet.global.common.response.exception.GlobalErrorException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;

@Service
@RequiredArgsConstructor
public class OauthSearchService {
    private final OauthJpaRepository oauthRepository;

    @Transactional(readOnly = true)
    public boolean isExistMember(BigInteger oauthId, ProviderType provider) {
        return oauthRepository.existsByOauthIdAndProvider(oauthId, provider);
    }

    @Transactional(readOnly = true)
    public boolean isExistEmail(String email) {
        return oauthRepository.existsByEmail(email);
    }

    @Transactional(readOnly = true)
    public Member findMemberByOauthIdAndProvider(BigInteger oauthId, ProviderType provider) {
        OauthAccount oauthAccount = oauthRepository.findByOauthIdAndProvider(oauthId, provider)
                .orElseThrow(() -> new GlobalErrorException(OauthException.NOT_FOUND_MEMBER));
        return oauthAccount.getMember();
    }
}
