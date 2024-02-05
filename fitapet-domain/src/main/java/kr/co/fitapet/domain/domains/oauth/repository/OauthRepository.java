package kr.co.fitapet.domain.domains.oauth.repository;

import com.kcy.fitapet.domain.oauth.domain.OauthAccount;
import com.kcy.fitapet.domain.oauth.type.ProviderType;
import com.kcy.fitapet.global.common.repository.ExtendedJpaRepository;

import java.util.Optional;

public interface OauthRepository extends ExtendedJpaRepository<OauthAccount, Long> {
    Optional<OauthAccount> findByOauthIdAndProvider(String oauthId, ProviderType provider);
    boolean existsByOauthIdAndProvider(String oauthId, ProviderType provider);
    boolean existsByEmail(String email);
}
