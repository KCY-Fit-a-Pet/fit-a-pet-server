package kr.co.fitapet.domain.common.redis.oauth;

import kr.co.fitapet.common.execption.GlobalErrorException;
import kr.co.fitapet.domain.common.redis.exception.RedisErrorCode;
import kr.co.fitapet.domain.common.redis.exception.RedisErrorException;
import kr.co.fitapet.domain.domains.oauth.type.ProviderType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
@Slf4j
public class OIDCTokenServiceImpl implements OIDCTokenService {
    private final OIDCTokenRepository oidcTokenRepository;

    @Override
    public void saveOIDCToken(String token, ProviderType provider, String id) {
        final long timeToLive = Duration.ofMinutes(3).toSeconds();

        log.info("oidc token ttl : {}", timeToLive);

        OIDCToken oidcToken = OIDCToken.of(token, provider, id, timeToLive);
        oidcTokenRepository.save(oidcToken);
        log.info("oidc token registered. about User : {}", oidcToken.getId());
    }

    @Override
    public OIDCToken findOIDCToken(String token) {
        return oidcTokenRepository.findById(token).orElseThrow(
                () -> new RedisErrorException(RedisErrorCode.NOT_FOUND_KEY)
        );
    }

    @Override
    public boolean existsOIDCToken(String token) {
        return oidcTokenRepository.existsById(token);
    }

    @Override
    public void deleteOIDCToken(String token) {
        oidcTokenRepository.deleteById(token);
    }
}
