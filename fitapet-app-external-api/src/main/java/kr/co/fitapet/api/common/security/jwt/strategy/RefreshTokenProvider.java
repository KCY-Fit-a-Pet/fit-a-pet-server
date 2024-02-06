package kr.co.fitapet.api.common.security.jwt.strategy;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import kr.co.fitapet.api.common.security.jwt.JwtProvider;
import kr.co.fitapet.api.common.security.jwt.dto.JwtSubInfo;
import kr.co.fitapet.api.common.security.jwt.dto.JwtUserInfo;
import kr.co.fitapet.api.common.security.jwt.exception.AuthErrorCode;
import kr.co.fitapet.api.common.security.jwt.exception.AuthErrorException;
import kr.co.fitapet.api.common.security.jwt.exception.JwtErrorCodeUtil;
import kr.co.fitapet.api.common.security.jwt.qualifier.RefreshTokenQualifier;
import kr.co.fitapet.common.date.DateUtil;
import kr.co.fitapet.domain.domains.member.type.RoleType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Date;
import java.util.Map;
import java.util.stream.Stream;

import static kr.co.fitapet.api.common.security.jwt.JwtField.ROLE;
import static kr.co.fitapet.api.common.security.jwt.JwtField.USER_ID;

@Slf4j
@Component
@RefreshTokenQualifier
public class RefreshTokenProvider implements JwtProvider {
    private final Key signatureKey;
    private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
    private final Duration tokenExpiration;

    public RefreshTokenProvider(
            @Value("${jwt.secret.refresh}") String jwtSecretKey,
            @Value("${jwt.token.refresh-expiration-time}") Duration tokenExpiration
    ) {
        final byte[] secretKeyBytes = Base64.getDecoder().decode(jwtSecretKey);
        this.signatureKey = Keys.hmacShaKeyFor(secretKeyBytes);
        this.tokenExpiration = tokenExpiration;
    }

    @Override
    public String resolveToken(String authHeader) {
        return JwtProvider.super.resolveToken(authHeader);
    }

    @Override
    public String generateToken(JwtSubInfo subs) {
        final Date now = new Date();

        return Jwts.builder()
                .setHeader(createHeader())
                .setClaims(createClaims(subs))
                .signWith(signatureKey, signatureAlgorithm)
                .setExpiration(createExpireDate(now, tokenExpiration.toMillis()))
                .compact();
    }

    @Override
    public JwtSubInfo getSubInfoFromToken(String token) {
        Claims claims = getClaimsFromToken(token);
        String role = claims.get(ROLE.getValue(), String.class);
        RoleType roleType = Stream.of(RoleType.values())
                .filter(r -> r.getRole().equals(role))
                .findFirst().orElseThrow(
                        () -> new AuthErrorException(AuthErrorCode.FAILED_AUTHENTICATION, "Invalid Role")
                );

        return JwtUserInfo.builder()
                .id(claims.get(USER_ID.getValue(), Long.class))
                .role(roleType)
                .build();
    }

    @Override
    public Claims getClaimsFromToken(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(signatureKey).build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (JwtException e) {
            final AuthErrorCode errorCode = JwtErrorCodeUtil.determineErrorCode(e, AuthErrorCode.FAILED_AUTHENTICATION);

            log.warn("Error code : {}, Error - {},  {}", errorCode, e.getClass(), e.getMessage());
            throw new AuthErrorException(errorCode, e.toString());
        }
    }

    @Override
    public LocalDateTime getExpiryDate(String token) throws AuthErrorException {
        Claims claims = getClaimsFromToken(token);
        return DateUtil.toLocalDateTime(claims.getExpiration());
    }

    @Override
    public boolean isTokenExpired(String token) {
        try {
            Claims claims = getClaimsFromToken(token);
            return claims.getExpiration().before(new Date());
        } catch (ExpiredJwtException e) {
            return true;
        }
    }

    private Map<String, Object> createHeader() {
        return Map.of("typ", "JWT",
                "alg", "HS256",
                "regDate", System.currentTimeMillis());
    }

    private Map<String, Object> createClaims(JwtSubInfo dto) {
        return Map.of(USER_ID.getValue(), dto.id(),
                ROLE.getValue(), dto.role().getRole());
    }

    private Date createExpireDate(final Date now, long expirationTime) {
        return new Date(now.getTime() + expirationTime);
    }
}
