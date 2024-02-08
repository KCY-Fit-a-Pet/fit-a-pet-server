package kr.co.fitapet.domain.domains.oauth.repository;

import kr.co.fitapet.domain.common.repository.ExtendedJpaRepository;
import kr.co.fitapet.domain.domains.oauth.domain.OauthAccount;
import kr.co.fitapet.domain.domains.oauth.type.ProviderType;

import java.util.Optional;

public interface OauthRepository extends ExtendedJpaRepository<OauthAccount, Long> {
    Optional<OauthAccount> findByOauthIdAndProvider(String oauthId, ProviderType provider);
    boolean existsByOauthIdAndProvider(String oauthId, ProviderType provider);
    boolean existsByEmail(String email);
}
