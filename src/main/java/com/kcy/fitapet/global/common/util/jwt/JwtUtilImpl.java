package com.kcy.fitapet.global.common.util.jwt;

import com.kcy.fitapet.domain.member.domain.RoleType;
import com.kcy.fitapet.global.common.util.jwt.entity.JwtUserInfo;
import com.kcy.fitapet.global.common.util.jwt.exception.AuthErrorCode;
import com.kcy.fitapet.global.common.util.jwt.exception.AuthErrorException;
import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.time.Duration;
import java.util.Base64;
import java.util.Date;
import java.util.Map;

/**
 * JWT 토큰 생성 및 검증을 담당하는 클래스
 */
@Slf4j
@Component
public class JwtUtilImpl implements JwtUtil {
    private static final String USER_ID = "userId";
    private static final String ROLE = "role";

    private final String jwtSecretKey;
    private final Duration accessTokenExpirationTime;
    private final Duration refreshTokenExpirationTime;

    public JwtUtilImpl(
            @Value("${jwt.secret}") String jwtSecretKey,
            @Value("${jwt.token.access-expiration-time}") Duration accessTokenExpirationTime,
            @Value("${jwt.token.refresh-expiration-time}") Duration refreshTokenExpirationTime
    ) {
        this.jwtSecretKey = jwtSecretKey;
        this.accessTokenExpirationTime = accessTokenExpirationTime;
        this.refreshTokenExpirationTime = refreshTokenExpirationTime;
    }

    @Override
    public String resolveToken(String authHeader) throws AuthErrorException {
        if (StringUtils.hasText(authHeader) && authHeader.startsWith(AuthConstants.TOKEN_TYPE.getValue())) {
            return authHeader.substring(AuthConstants.TOKEN_TYPE.getValue().length());
        }
        throw new AuthErrorException(AuthErrorCode.EMPTY_ACCESS_TOKEN, "Access Token is empty.");
    }

    @SuppressWarnings("deprecation")
    @Override
    public String generateAccessToken(JwtUserInfo user) {
        final Date now = new Date();

        return Jwts.builder()
                .setHeader(createHeader())
                .setClaims(createClaims(user))
                .signWith(SignatureAlgorithm.HS256, createSignature())
                .setExpiration(createExpireDate(now, accessTokenExpirationTime.toMillis()))
                .compact();
    }

    @Override
    public String generateRefreshToken(JwtUserInfo user) {
        final Date now = new Date();

        return Jwts.builder()
                .setHeader(createHeader())
                .setClaims(createClaims(user))
                .signWith(SignatureAlgorithm.HS256, createSignature())
                .setExpiration(createExpireDate(now, refreshTokenExpirationTime.toMillis()))
                .compact();
    }

    @Override
    public JwtUserInfo getUserInfoFromToken(String token) throws AuthErrorException {
        Claims claims = verifyAndGetClaims(token);
        return JwtUserInfo.builder()
                .id(claims.get(USER_ID, Long.class))
                .role(RoleType.fromString(claims.get(ROLE, String.class)))
                .build();
    }

    @Override
    public Long getUserIdFromToken(String token) throws AuthErrorException {
        Claims claims = verifyAndGetClaims(token);
        return claims.get(USER_ID, Long.class);
    }

    @Override
    public Date getExpiryDate(String token) {
        Claims claims = verifyAndGetClaims(token);
        return claims.getExpiration();
    }

    private Claims verifyAndGetClaims(final String accessToken) throws AuthErrorException {
        try {
            return getClaimsFromToken(accessToken);
        } catch (JwtException e) {
            handleJwtException(e);
        }
        throw new IllegalStateException("Unreachable code reached.");
    }

    private void handleJwtException(JwtException e) {
        AuthErrorCode errorCode;
        String causedBy;
        if (e instanceof ExpiredJwtException) {
            errorCode = AuthErrorCode.EXPIRED_ACCESS_TOKEN;
            causedBy = e.toString();
        } else if (e instanceof MalformedJwtException) {
            errorCode = AuthErrorCode.MALFORMED_ACCESS_TOKEN;
            causedBy = e.toString();
        } else if (e instanceof SignatureException) {
            errorCode = AuthErrorCode.TAMPERED_ACCESS_TOKEN;
            causedBy = e.toString();
        } else if (e instanceof UnsupportedJwtException) {
            errorCode = AuthErrorCode.WRONG_JWT_TOKEN;
            causedBy = e.toString();
        } else {
            errorCode = AuthErrorCode.EMPTY_ACCESS_TOKEN;
            causedBy = e.toString();
        }

        log.warn(causedBy);
        throw new AuthErrorException(errorCode, causedBy);
    }

    private Map<String, Object> createHeader() {
        return Map.of("typ", "JWT",
                "alg", "HS256",
                "regDate", System.currentTimeMillis());
    }

    private Map<String, Object> createClaims(JwtUserInfo dto) {
        return Map.of(USER_ID, dto.id(),
                ROLE, dto.role().getRole());
    }

    private Key createSignature() {
        byte[] secretKeyBytes = Base64.getDecoder().decode(jwtSecretKey);
        return new SecretKeySpec(secretKeyBytes, SignatureAlgorithm.HS256.getJcaName());
    }

    private Date createExpireDate(final Date now, long expirationTime) {
        return new Date(now.getTime() + expirationTime);
    }

    @SuppressWarnings("deprecation")
    private Claims getClaimsFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(Base64.getDecoder().decode(jwtSecretKey))
                .parseClaimsJws(token)
                .getBody();
    }
}