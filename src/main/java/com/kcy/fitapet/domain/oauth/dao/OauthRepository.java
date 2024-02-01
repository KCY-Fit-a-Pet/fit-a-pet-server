package com.kcy.fitapet.domain.oauth.dao;

import com.kcy.fitapet.domain.oauth.domain.OauthAccount;
import com.kcy.fitapet.domain.oauth.type.ProviderType;
import com.kcy.fitapet.global.common.repository.ExtendedJpaRepository;

import java.math.BigInteger;
import java.util.Optional;

public interface OauthRepository extends ExtendedJpaRepository<OauthAccount, Long> {
    Optional<OauthAccount> findByOauthIdAndProvider(BigInteger oauthId, ProviderType provider);
    boolean existsByOauthIdAndProvider(BigInteger oauthId, ProviderType provider);
    boolean existsByEmail(String email);
}
