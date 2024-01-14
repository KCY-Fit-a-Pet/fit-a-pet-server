package com.kcy.fitapet.global.common.redis.oauth;

import com.kcy.fitapet.domain.oauth.exception.OauthException;
import com.kcy.fitapet.domain.oauth.type.ProviderType;
import com.kcy.fitapet.global.common.response.exception.GlobalErrorException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.time.Duration;
import java.time.LocalDateTime;

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
                () -> new GlobalErrorException(OauthException.NOT_FOUND_ID_TOKEN)
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
