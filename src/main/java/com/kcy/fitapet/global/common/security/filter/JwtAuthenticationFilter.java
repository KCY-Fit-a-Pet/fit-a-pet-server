package com.kcy.fitapet.global.common.security.filter;

import com.kcy.fitapet.global.common.security.authentication.UserDetailServiceImpl;
import com.kcy.fitapet.global.common.util.cookie.CookieUtil;
import com.kcy.fitapet.global.common.security.jwt.JwtUtil;
import com.kcy.fitapet.global.common.security.jwt.dto.JwtUserInfo;
import com.kcy.fitapet.global.common.security.jwt.exception.AuthErrorCode;
import com.kcy.fitapet.global.common.security.jwt.exception.AuthErrorException;
import com.kcy.fitapet.global.common.redis.forbidden.ForbiddenTokenService;
import com.kcy.fitapet.global.common.redis.refresh.RefreshToken;
import com.kcy.fitapet.global.common.redis.refresh.RefreshTokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.regex.Pattern;

import static com.kcy.fitapet.global.common.security.jwt.AuthConstants.*;

/**
 * JWT 인증 필터 <br/>
 * 만약, 액세스 토큰과 리프레시 토큰이 모두 없다면 익명 사용자로 간주한다. <br/>
 * 액세스 토큰이 없다면(혹은 만료) 리프레시 토큰을 이용해 액세스 토큰을 재발급한다. <br/>
 * 액세스 토큰과 리프레시 토큰 모두 만료되었다면, 예외 응답을 전송하여 재로그인 하도록 유도한다. <br/>
 */
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final UserDetailServiceImpl userDetailServiceImpl;
    private final RefreshTokenService refreshTokenService;
    private final ForbiddenTokenService forbiddenTokenService;

    private final JwtUtil jwtUtil;
    private final CookieUtil cookieUtil;

    private List<String> jwtIgnoreUrls = List.of(
            "/api/v1/auth/oauth/**"
    );

    @Override
    protected void doFilterInternal(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull FilterChain filterChain) throws ServletException, IOException {
        if (shouldIgnoreRequest(request)) {
            log.info("Ignoring request: {}", request.getRequestURI());
            filterChain.doFilter(request, response);
            return;
        }

        if (isAnonymousRequest(request)) {
            filterChain.doFilter(request, response);
            return;
        }

        String accessToken = resolveAccessToken(request, response);

        UserDetails userDetails = getUserDetails(accessToken);
        authenticateUser(userDetails, request);
        filterChain.doFilter(request, response);
    }

    private boolean shouldIgnoreRequest(HttpServletRequest request) {
        String uri = request.getRequestURI();
        String method = request.getMethod();

        boolean isIgnored = jwtIgnoreUrls.stream()
                .anyMatch(pattern -> matchesPattern(uri, pattern));
        return isIgnored || "OPTIONS".equals(method);
    }

    private boolean matchesPattern(String uri, String pattern) {
        return Pattern.matches(pattern.replace("**", ".*"), uri) ||
                Pattern.matches(pattern.replace("/**", ""), uri);
    }

    private boolean isAnonymousRequest(HttpServletRequest request) {
        String accessToken = request.getHeader(AUTH_HEADER.getValue());
        String refreshToken = request.getHeader(REFRESH_TOKEN.getValue());

        return accessToken == null && refreshToken == null;
    }

    private String resolveAccessToken(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        String authHeader = request.getHeader(AUTH_HEADER.getValue());

        String token = jwtUtil.resolveToken(authHeader);
        if (!StringUtils.hasText(token))
            handleAuthException(AuthErrorCode.EMPTY_ACCESS_TOKEN, "액세스 토큰이 없습니다.");

        if (forbiddenTokenService.isForbidden(token))
            handleAuthException(AuthErrorCode.FORBIDDEN_ACCESS_TOKEN, "더 이상 사용할 수 없는 토큰입니다.");

        if (jwtUtil.isTokenExpired(token)) {
            log.warn("Expired JWT access token: {}", token);
            return reissueAccessToken(request, response);
        }

        return token;
    }

    private String reissueAccessToken(HttpServletRequest request, HttpServletResponse response) {
        Cookie refreshTokenCookie = cookieUtil.getCookieFromRequest(request, REFRESH_TOKEN.getValue())
                .orElseThrow(() -> new AuthErrorException(AuthErrorCode.REFRESH_TOKEN_NOT_FOUND, "Refresh token not found"));
        String requestRefreshToken = refreshTokenCookie.getValue();

        RefreshToken reissuedRefreshToken = refreshTokenService.refresh(requestRefreshToken);
        ResponseCookie cookie = cookieUtil.createCookie(REFRESH_TOKEN.getValue(), reissuedRefreshToken.getToken(), refreshTokenCookie.getMaxAge());
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());

        JwtUserInfo userInfo = jwtUtil.getUserInfoFromToken(requestRefreshToken);
        String reissuedAccessToken = jwtUtil.generateAccessToken(userInfo);
        response.addHeader(REISSUED_ACCESS_TOKEN.getValue(), reissuedAccessToken);
        return reissuedAccessToken;
    }

    private UserDetails getUserDetails(final String accessToken) {
        Long userId = jwtUtil.getUserIdFromToken(accessToken);
        return userDetailServiceImpl.loadUserByUsername(userId.toString());
    }

    private void authenticateUser(UserDetails userDetails, HttpServletRequest request) {
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities()
        );

        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        log.info("Authenticated user: {}", userDetails.getUsername());
    }

    private void handleAuthException(AuthErrorCode errorCode, String errorMessage) throws ServletException {
        log.warn("AuthErrorException(code={}, message={})", errorCode.name(), errorMessage);
        AuthErrorException exception = new AuthErrorException(errorCode, errorMessage);
        throw new ServletException(exception);
    }
}