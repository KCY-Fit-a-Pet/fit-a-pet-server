package kr.co.fitapet.api.common.security.jwt.strategy;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import kr.co.fitapet.api.common.security.jwt.JwtProvider;
import kr.co.fitapet.api.common.security.jwt.dto.JwtSubInfo;
import kr.co.fitapet.api.common.security.jwt.dto.SmsAuthInfo;
import kr.co.fitapet.api.common.security.jwt.exception.AuthErrorCode;
import kr.co.fitapet.api.common.security.jwt.exception.AuthErrorException;
import kr.co.fitapet.api.common.security.jwt.exception.JwtErrorCodeUtil;
import kr.co.fitapet.api.common.security.jwt.qualifier.SmsAuthTokenQualifier;
import kr.co.fitapet.common.date.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Date;
import java.util.Map;

import static kr.co.fitapet.api.common.security.jwt.consts.JwtField.PHONE_NUMBER;

@Slf4j
@Component
@SmsAuthTokenQualifier
public class SmsAuthTokenProvider implements JwtProvider {
    private final Key signatureKey;
    private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
    private final Duration tokenExpiration;

    public SmsAuthTokenProvider(
            @Value("${jwt.secret.sms-auth}") String jwtSecretKey,
            @Value("${jwt.token.sms-auth-expiration-time}") Duration tokenExpiration
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
        log.info("smsAuthTokenProvider generate");
        final Date now = new Date();

        return Jwts.builder()
                .setHeader(createHeader())
                .setClaims(createClaims(subs))
                .signWith(signatureKey, signatureAlgorithm)
                .setExpiration(createExpireDate(now, tokenExpiration.toMillis()))
                .compact();
    }

    public JwtSubInfo getSubInfoFromToken(String token) {
        Claims claims = getClaimsFromToken(token);
        return SmsAuthInfo.of(1L, claims.get(PHONE_NUMBER.getValue(), String.class));
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
        throw new UnsupportedOperationException();
    }

    private Map<String, Object> createHeader() {
        return Map.of("typ", "JWT",
                "alg", "HS256",
                "regDate", System.currentTimeMillis());
    }

    private Map<String, Object> createClaims(JwtSubInfo dto) {
        return Map.of(PHONE_NUMBER.getValue(), dto.phoneNumber());
    }

    private Date createExpireDate(final Date now, long expirationTime) {
        return new Date(now.getTime() + expirationTime);
    }
}
