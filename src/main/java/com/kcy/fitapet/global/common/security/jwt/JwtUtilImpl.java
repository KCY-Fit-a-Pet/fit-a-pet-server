package com.kcy.fitapet.global.common.security.jwt;

import com.kcy.fitapet.domain.member.type.RoleType;
import com.kcy.fitapet.global.common.util.DateUtil;
import com.kcy.fitapet.global.common.security.jwt.exception.JwtErrorCodeUtil;
import com.kcy.fitapet.global.common.security.jwt.dto.JwtUserInfo;
import com.kcy.fitapet.global.common.security.jwt.dto.SmsAuthInfo;
import com.kcy.fitapet.global.common.security.jwt.exception.AuthErrorCode;
import com.kcy.fitapet.global.common.security.jwt.exception.AuthErrorException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.security.Key;
import java.time.Duration;
import java.time.LocalDateTime;
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
    private static final String PHONE_NUMBER = "phoneNumber";

    private final Key signatureKey;
    private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
    private final JwtApplicationConfig jwtApplicationConfig;

    private final String smsAuthSecretKey;
    private final String smsOauthAccessKey;

    public JwtUtilImpl(
            @Value("${jwt.secret.default}") String jwtSecretKey,
            @Value("${jwt.secret.sms-auth}") String smsAuthSecretKey,
            @Value("${jwt.secret.sms-oauth}") String smsOauthAccessKey,
            @Autowired JwtApplicationConfig jwtApplicationConfig
    ) {
        final byte[] secretKeyBytes = Base64.getDecoder().decode(jwtSecretKey);
        this.signatureKey = Keys.hmacShaKeyFor(secretKeyBytes);
        this.smsAuthSecretKey = smsAuthSecretKey;
        this.smsOauthAccessKey = smsOauthAccessKey;
        this.jwtApplicationConfig = jwtApplicationConfig;
    }

    @Override
    public String resolveToken(String authHeader) {
        if (StringUtils.hasText(authHeader) && authHeader.startsWith(AuthConstants.TOKEN_TYPE.getValue())) {
            return authHeader.substring(AuthConstants.TOKEN_TYPE.getValue().length());
        }
        return "";
    }

    @Override
    public String generateAccessToken(JwtUserInfo user) {
        final Date now = new Date();

        return Jwts.builder()
                .setHeader(createHeader())
                .setClaims(createClaims(user))
                .signWith(signatureKey, signatureAlgorithm)
                .setExpiration(createExpireDate(now, jwtApplicationConfig.getAccessExpirationTime().toMillis()))
                .compact();
    }

    @Override
    public String generateRefreshToken(JwtUserInfo user) {
        final Date now = new Date();

        return Jwts.builder()
                .setHeader(createHeader())
                .setClaims(createClaims(user))
                .signWith(signatureKey, signatureAlgorithm)
                .setExpiration(createExpireDate(now, jwtApplicationConfig.getRefreshExpirationTime().toMillis()))
                .compact();
    }

    @Override
    public String generateSmsAuthToken(SmsAuthInfo user) {
        final byte[] secretKeyBytes = Base64.getDecoder().decode(smsAuthSecretKey);
        final Key signatureKey = Keys.hmacShaKeyFor(secretKeyBytes);
        final Date now = new Date();

        return Jwts.builder()
                .setHeader(createHeader())
                .setClaims(Map.of(PHONE_NUMBER, user.phoneNumber()))
                .signWith(signatureKey, signatureAlgorithm)
                .setExpiration(createExpireDate(now, jwtApplicationConfig.getSmsAuthExpirationTime().toMillis()))
                .compact();
    }

    @Override
    public String generateSmsOauthToken(SmsAuthInfo user) {
        final byte[] secretKeyBytes = Base64.getDecoder().decode(smsOauthAccessKey);
        final Key signatureKey = Keys.hmacShaKeyFor(secretKeyBytes);
        final Date now = new Date();

        return Jwts.builder()
                .setHeader(createHeader())
                .setClaims(Map.of(USER_ID, user.userId(), PHONE_NUMBER, user.phoneNumber()))
                .signWith(signatureKey, signatureAlgorithm)
                .setExpiration(createExpireDate(now, jwtApplicationConfig.getSmsAuthExpirationTime().toMillis()))
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
    public String getPhoneNumberFromToken(String token) throws AuthErrorException {
        Claims claims = verifyAndGetClaims(token);
        return claims.get(PHONE_NUMBER, String.class);
    }

    @Override
    public LocalDateTime getExpiryDate(String token) {
        Claims claims = verifyAndGetClaims(token);
        return DateUtil.toLocalDateTime(claims.getExpiration());
    }

    @Override
    public boolean isTokenExpired(String token) {
        try {
            Claims claims = verifyAndGetClaims(token);
            return claims.getExpiration().before(new Date());
        } catch (AuthErrorException e) {
            return true;
        }
    }

    private Claims verifyAndGetClaims(final String accessToken) {
        try {
            return getClaimsFromToken(accessToken);
        } catch (JwtException e) {
            final AuthErrorCode errorCode = JwtErrorCodeUtil.determineErrorCode(e, AuthErrorCode.FAILED_AUTHENTICATION);

            log.warn("Error code : {}, Error - {},  {}", errorCode, e.getClass(), e.getMessage());
            throw new AuthErrorException(errorCode, e.toString());
        }
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

    private Date createExpireDate(final Date now, long expirationTime) {
        return new Date(now.getTime() + expirationTime);
    }

    private Claims getClaimsFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(signatureKey).build()
                .parseClaimsJws(token)
                .getBody();
    }
}