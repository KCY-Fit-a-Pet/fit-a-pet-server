package com.kcy.fitapet.global.common.redis.oauth;

import com.kcy.fitapet.domain.oauth.type.ProviderType;

import java.math.BigInteger;

public interface OIDCTokenService {
    /**
     * OIDC 토큰을 저장
     * @param token : OIDC 토큰
     * @param provider : 제공자
     * @param id : 제공자의 유저 고유번호
     */
    void saveOIDCToken(String token, ProviderType provider, String id);

    /**
     * OIDC 토큰을 찾아서 반환
     * @param token : OIDC 토큰
     * @return OIDCToken
     */
    OIDCToken findOIDCToken(String token);

    /**
     * OIDC 토큰이 존재하는지 확인
     * @param token : OIDC 토큰
     * @return boolean
     */
    boolean existsOIDCToken(String token);

    /**
     * OIDC 토큰을 삭제
     * @param token : OIDC 토큰
     */
    void deleteOIDCToken(String token);
}
