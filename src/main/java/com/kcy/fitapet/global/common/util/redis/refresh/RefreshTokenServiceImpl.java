package com.kcy.fitapet.global.common.util.redis.refresh;

import com.kcy.fitapet.global.common.util.jwt.JwtUtil;
import com.kcy.fitapet.global.common.util.jwt.entity.JwtUserInfo;
import com.kcy.fitapet.global.common.util.jwt.exception.AuthErrorCode;
import com.kcy.fitapet.global.common.util.jwt.exception.AuthErrorException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Slf4j
@Service
public class RefreshTokenServiceImpl implements RefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtUtil jwtUtil;
    private final Duration refreshTokenExpireTime;

    public RefreshTokenServiceImpl(
            RefreshTokenRepository refreshTokenRepository,
            JwtUtil jwtUtil,
            @Value("${jwt.token.refresh-expiration-time}") Duration refreshTokenExpireTime)
    {
        this.refreshTokenRepository = refreshTokenRepository;
        this.jwtUtil = jwtUtil;
        this.refreshTokenExpireTime = refreshTokenExpireTime;
    }

    @Override
    public String issueRefreshToken(String accessToken) throws AuthErrorException {
        final var user = jwtUtil.getUserInfoFromToken(accessToken);

        final var refreshToken = RefreshToken.builder()
                .userId(user.id())
                .token(makeRefreshToken(user))
                .ttl(getExpireTime())
                .build();

        refreshTokenRepository.save(refreshToken);
        log.debug("refresh token issued. : {}", refreshToken);
        return refreshToken.getToken();
    }

    @Override
    public RefreshToken refresh(String requestRefreshToken) throws AuthErrorException {
        final Long userId = jwtUtil.getUserIdFromToken(requestRefreshToken);
        final RefreshToken refreshToken = findOrThrow(userId);

        validateToken(requestRefreshToken, refreshToken);

        final var user = jwtUtil.getUserInfoFromToken(requestRefreshToken);
        refreshToken.rotation(makeRefreshToken(user));
        // TODO: TTL이 유지가 안 될 가능성 존재. TTL 필드 말고, Redis Template의 expire 메서드 사용하는 게 정확할 듯
        refreshTokenRepository.save(refreshToken);

        log.debug("refresh token reissued. : {}", refreshToken);
        return refreshToken;
    }

    @Override
    public void logout(String requestRefreshToken) {
        final Long userId = jwtUtil.getUserIdFromToken(requestRefreshToken);
        final RefreshToken refreshToken = findOrThrow(userId);

        refreshTokenRepository.delete(refreshToken);
        log.info("refresh token deleted. : {}", refreshToken);
    }

    private String makeRefreshToken(JwtUserInfo user) {
        return jwtUtil.generateRefreshToken(user);
    }

    private long getExpireTime() {
        return refreshTokenExpireTime.toMillis() / 1000;
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