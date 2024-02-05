package kr.co.fitapet.domain.common.redis.refresh;

import com.kcy.fitapet.global.common.security.jwt.JwtProvider;
import com.kcy.fitapet.global.common.security.jwt.dto.JwtSubInfo;
import com.kcy.fitapet.global.common.security.jwt.exception.AuthErrorCode;
import com.kcy.fitapet.global.common.security.jwt.exception.AuthErrorException;
import com.kcy.fitapet.global.common.security.jwt.qualifier.AccessTokenQualifier;
import com.kcy.fitapet.global.common.security.jwt.qualifier.RefreshTokenQualifier;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Slf4j
@Service
public class RefreshTokenServiceImpl implements RefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtProvider jwtAccessTokenProvider;
    private final JwtProvider jwtRefreshTokenProvider;
    private final Duration refreshTokenExpireTime;

    public RefreshTokenServiceImpl(
            RefreshTokenRepository refreshTokenRepository,
            @AccessTokenQualifier JwtProvider jwtAccessTokenProvider,
            @RefreshTokenQualifier JwtProvider jwtRefreshTokenProvider,
            @Value("${jwt.token.refresh-expiration-time}") Duration refreshTokenExpireTime)
    {
        this.refreshTokenRepository = refreshTokenRepository;
        this.jwtAccessTokenProvider = jwtAccessTokenProvider;
        this.jwtRefreshTokenProvider = jwtRefreshTokenProvider;
        this.refreshTokenExpireTime = refreshTokenExpireTime;
    }

    @Override
    public String issueRefreshToken(String accessToken) throws AuthErrorException {
        JwtSubInfo subs = jwtAccessTokenProvider.getSubInfoFromToken(accessToken);

        final var refreshToken = RefreshToken.builder()
                .userId(subs.id())
                .token(makeRefreshToken(subs))
                .ttl(getExpireTime())
                .build();

        refreshTokenRepository.save(refreshToken);
        log.debug("refresh token issued. : {}", refreshToken);
        return refreshToken.getToken();
    }

    @Override
    public RefreshToken refresh(String requestRefreshToken) throws AuthErrorException {
        JwtSubInfo subs = jwtRefreshTokenProvider.getSubInfoFromToken(requestRefreshToken);
        RefreshToken refreshToken = findOrThrow(subs.id());

        validateToken(requestRefreshToken, refreshToken);

        refreshToken.rotation(makeRefreshToken(subs));
        refreshTokenRepository.save(refreshToken);

        log.debug("refresh token reissued. : {}", refreshToken);
        return refreshToken;
    }

    @Override
    public void logout(String requestRefreshToken) {
        JwtSubInfo subs = jwtRefreshTokenProvider.getSubInfoFromToken(requestRefreshToken);
        RefreshToken refreshToken = findOrThrow(subs.id());

        refreshTokenRepository.delete(refreshToken);
        log.info("refresh token deleted. : {}", refreshToken);
    }

    private String makeRefreshToken(JwtSubInfo subs) {
        return jwtRefreshTokenProvider.generateToken(subs);
    }

    private long getExpireTime() {
        return refreshTokenExpireTime.toSeconds();
    }

    private RefreshToken findOrThrow(Long userId) {
        return refreshTokenRepository.findById(userId)
                .orElseThrow(() -> new AuthErrorException(AuthErrorCode.REFRESH_TOKEN_NOT_FOUND, "can't find refresh token"));
    }

    private void validateToken(String requestRefreshToken, RefreshToken refreshToken) throws AuthErrorException {
        final String expectedRequestRefreshToken = refreshToken.getToken();

        if (isTakenAway(requestRefreshToken, expectedRequestRefreshToken)) {
            refreshTokenRepository.delete(refreshToken);

            final String errorMessage = String.format("mismatched refresh token. expected : %s, actual : %s", requestRefreshToken, expectedRequestRefreshToken);
            log.warn(errorMessage);
            log.info("refresh token deleted. : {}", refreshToken);
            throw new AuthErrorException(AuthErrorCode.MISMATCHED_REFRESH_TOKEN, errorMessage);
        }
    }

    private boolean isTakenAway(String requestRefreshToken, String expectedRequestRefreshToken) {
        return !requestRefreshToken.equals(expectedRequestRefreshToken);
    }
}