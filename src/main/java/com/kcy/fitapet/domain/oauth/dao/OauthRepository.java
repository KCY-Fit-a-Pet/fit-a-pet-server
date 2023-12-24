package com.kcy.fitapet.domain.oauth.dao;

import com.kcy.fitapet.domain.oauth.domain.OauthAccount;
import com.kcy.fitapet.domain.oauth.type.ProviderType;
import com.kcy.fitapet.global.common.repository.ExtendedRepository;

import java.util.Optional;

public interface OauthRepository extends ExtendedRepository<OauthAccount, Long> {
    Optional<OauthAccount> findByOauthIdAndProvider(Long oauthId, ProviderType provider);
    boolean existsByOauthIdAndProvider(Long oauthId, ProviderType provider);
    boolean existsByEmail(String email);
}
