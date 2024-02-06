package kr.co.fitapet.domain.common.redis.refresh;

import kr.co.fitapet.domain.common.redis.exception.RedisErrorException;

public interface RefreshTokenService {
    /**
     * 생성한 refresh token을 redis에 저장
     * @param refreshToken : 유저가 가지고 있는 refresh token
     * @return String : Refresh Token
     */
    String issueRefreshToken(RefreshToken refreshToken);

    /**
     * 사용자가 보낸 refresh token으로 기존 refresh token과 비교 검증 후, 새로운 refresh token으로 교체
     * @param requestRefreshToken : 사용자가 보낸 refresh token
     * @param newRefreshToken : 교체할 refresh token (jwt provider로 생성한 토큰이어야 함)
     * @return RefreshToken
     * @throws RedisErrorException : RedisErrorCode.NOT_FOUND_KEY(요청한 토큰과 저장된 토큰이 다르다면 토큰이 탈취되었다고 판단하여 값 삭제) ,
     * RedisErrorCode.MISS_MATCHED_VALUES(토큰이 만료되어 redis에 저장된 토큰이 없을 경우)
     */
    RefreshToken refresh(RefreshToken requestRefreshToken, String newRefreshToken) throws RedisErrorException;

    /**
     * access token 으로 refresh token을 찾아서 제거 (로그아웃)
     * @param requestRefreshToken : RefreshToken
     */
    void logout(RefreshToken requestRefreshToken);
}