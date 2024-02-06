package kr.co.fitapet.domain.common.redis.refresh;

import kr.co.fitapet.domain.common.redis.exception.RedisErrorCode;
import kr.co.fitapet.domain.common.redis.exception.RedisErrorException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Slf4j
@Service
@RequiredArgsConstructor
public class RefreshTokenServiceImpl implements RefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;

    @Override
    public String issueRefreshToken(RefreshToken refreshToken) {
        refreshTokenRepository.save(refreshToken);
        log.debug("refresh token issued. : {}", refreshToken);
        return refreshToken.getToken();
    }

    @Override
    public RefreshToken refresh(RefreshToken requestRefreshToken, String newRefreshToken) {
        RefreshToken refreshToken = findOrElseThrow(requestRefreshToken.getUserId());

        validateToken(requestRefreshToken.getToken(), refreshToken);

        refreshToken.rotation(newRefreshToken);
        refreshTokenRepository.save(refreshToken);

        log.debug("refresh token reissued. : {}", refreshToken);
        return refreshToken;
    }

    @Override
    public void logout(RefreshToken requestRefreshToken) {
        RefreshToken refreshToken = findOrElseThrow(requestRefreshToken.getUserId());

        refreshTokenRepository.delete(refreshToken);
        log.info("refresh token deleted. : {}", refreshToken);
    }

    private RefreshToken findOrElseThrow(Long userId) {
        return refreshTokenRepository.findById(userId)
                .orElseThrow(() -> new RedisErrorException(RedisErrorCode.NOT_FOUND_KEY));
    }

    private void validateToken(String requestRefreshToken, RefreshToken refreshToken) throws RedisErrorException {
        final String expectedRequestRefreshToken = refreshToken.getToken();

        if (isTakenAway(requestRefreshToken, expectedRequestRefreshToken)) {
            refreshTokenRepository.delete(refreshToken);

            final String errorMessage = String.format("mismatched refresh token. expected : %s, actual : %s", requestRefreshToken, expectedRequestRefreshToken);
            log.warn(errorMessage);
            log.info("refresh token deleted. : {}", refreshToken);
            throw new RedisErrorException(RedisErrorCode.MISS_MATCHED_VALUES);
        }
    }

    private boolean isTakenAway(String requestRefreshToken, String expectedRequestRefreshToken) {
        return !requestRefreshToken.equals(expectedRequestRefreshToken);
    }
}