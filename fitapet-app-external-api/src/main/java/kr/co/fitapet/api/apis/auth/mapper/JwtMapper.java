package kr.co.fitapet.api.apis.auth.mapper;

import kr.co.fitapet.api.common.security.jwt.JwtProvider;
import kr.co.fitapet.api.common.security.jwt.consts.JwtType;
import kr.co.fitapet.api.common.security.jwt.dto.Jwt;
import kr.co.fitapet.api.common.security.jwt.dto.JwtSubInfo;
import kr.co.fitapet.api.common.security.jwt.qualifier.AccessTokenQualifier;
import kr.co.fitapet.api.common.security.jwt.qualifier.RefreshTokenQualifier;
import kr.co.fitapet.api.common.security.jwt.qualifier.SmsAuthTokenQualifier;
import kr.co.fitapet.common.annotation.Mapper;
import kr.co.fitapet.domain.common.redis.forbidden.ForbiddenTokenService;
import kr.co.fitapet.domain.common.redis.refresh.RefreshToken;
import kr.co.fitapet.domain.common.redis.refresh.RefreshTokenService;
import kr.co.fitapet.domain.common.redis.AccessToken;
import org.springframework.util.StringUtils;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Map;

import static kr.co.fitapet.api.common.security.jwt.consts.JwtType.*;

@Mapper
public class JwtMapper {
    private final Map<JwtType, JwtProvider> jwtProviderMap;
    private final RefreshTokenService refreshTokenService;
    private final ForbiddenTokenService forbiddenTokenService;

    public JwtMapper(
            @AccessTokenQualifier JwtProvider accessTokenProvider,
            @RefreshTokenQualifier JwtProvider refreshTokenProvider,
            @SmsAuthTokenQualifier JwtProvider smsAuthTokenProvider,
            @SmsAuthTokenQualifier JwtProvider smsOauthTokenProvider,
            RefreshTokenService refreshTokenService,
            ForbiddenTokenService forbiddenTokenService
    ) {
        jwtProviderMap = Map.of(
                ACCESS_TOKEN, accessTokenProvider,
                REFRESH_TOKEN, refreshTokenProvider,
                SMS_AUTH_TOKEN, smsAuthTokenProvider,
                SMS_OAUTH_TOKEN, smsOauthTokenProvider
        );
        this.refreshTokenService = refreshTokenService;
        this.forbiddenTokenService = forbiddenTokenService;
    }

    /**
     * 사용자 정보 기반으로 access token과 refresh token을 생성하는 메서드 <br/>
     * refresh token은 redis에 저장된다.
     * @param sub : 토큰 payload에 담을 정보
     * @return : 생성된 토큰
     */
    public Jwt login(JwtSubInfo sub) {
        String accessToken = jwtProviderMap.get(ACCESS_TOKEN).generateToken(sub);
        String refreshToken = jwtProviderMap.get(REFRESH_TOKEN).generateToken(sub);
        refreshTokenService.issueRefreshToken(getRefreshTokenEntity(refreshToken));

        return Jwt.of(accessToken, refreshToken);
    }

    public void logout(AccessToken accessToken, String refreshToken) {
        forbiddenTokenService.register(accessToken);
        if (!StringUtils.hasText(refreshToken)) {
            RefreshToken refreshTokenEntity = getRefreshTokenEntity(refreshToken);
            refreshTokenService.logout(refreshTokenEntity);
        }
    }

    public Jwt refresh(String requestRefreshToken) {
        RefreshToken refreshTokenEntity = getRefreshTokenEntity(requestRefreshToken);
        JwtSubInfo info = getSubInfoFromToken(requestRefreshToken, REFRESH_TOKEN);

        RefreshToken reissuedRefreshToken = refreshTokenService.refresh(refreshTokenEntity, generateToken(info, REFRESH_TOKEN));
        return Jwt.of(generateToken(info, ACCESS_TOKEN), reissuedRefreshToken.getToken());
    }

    public boolean isForbidden(String token) {
        return forbiddenTokenService.isForbidden(token);
    }

    /**
     * 사용자 정보 기반으로 토큰을 생성하는 메서드
     * @param sub : 토큰 payload에 담을 정보
     * @param type : 토큰의 타입
     * @return : 생성된 토큰
     */
    public String generateToken(JwtSubInfo sub, JwtType type) {
        return jwtProviderMap.get(type).generateToken(sub);
    }

    /**
     * header에서 token을 추출하는 메서드
     * @param header : Authorization header
     * @param type : 추출할 token의 타입
     * @return : 추출된 token
     */
    public String resolveToken(String header, JwtType type) {
        return jwtProviderMap.get(type).resolveToken(header);
    }


    /**
     * 토큰으로 부터 사용자 정보를 추출하여 JwtSubInfo 객체로 반환하는 메서드
     * @param token : 토큰
     * @param type : 토큰의 타입
     * @return : 토큰의 payload에 담긴 사용자 정보
     */
    public JwtSubInfo getSubInfoFromToken(String token, JwtType type) {
        return jwtProviderMap.get(type).getSubInfoFromToken(token);
    }

    /**
     * 토큰으로 부터 사용자 정보를 추출하는 메서드
     * @param token : 토큰
     * @param type : 토큰의 타입
     * @return : 사용자 정보
     */
    public LocalDateTime getExpiryDate(String token, JwtType type) {
        return jwtProviderMap.get(type).getExpiryDate(token);
    }

    /**
     * 토큰의 만료 여부를 검사하는 메서드
     * @param token : 검사할 토큰
     * @param type : 검사할 토큰의 타입
     * @return : 만료 여부
     */
    public boolean isTokenExpired(String token, JwtType type) {
        return jwtProviderMap.get(type).isTokenExpired(token);
    }

    /**
     * 토큰을 블랙 리스트에 등록하는 메서드
     * @param token : 블랙 리스트에 등록할 토큰
     * @param type : 토큰의 타입
     */
    public void ban(String token, JwtType type) {
        AccessToken forbiddenToken = AccessToken.of(token, getSubInfoFromToken(token, type).id(), getExpiryDate(token, type));
        forbiddenTokenService.register(forbiddenToken);
    }

    /**
     * refresh token을 통해 refresh token entity를 생성하는 메서드
     * @param token : refresh token
     * @return : refresh token entity
     */
    public RefreshToken getRefreshTokenEntity(String token) {
        JwtSubInfo info = getSubInfoFromToken(token, REFRESH_TOKEN);
        return RefreshToken.of(info.id(), token, toSecond(getExpiryDate(token, REFRESH_TOKEN)));
    }

    private long toSecond(LocalDateTime target) {
        LocalDateTime now = LocalDateTime.now();
        return Duration.between(now, target).getSeconds();
    }
}
