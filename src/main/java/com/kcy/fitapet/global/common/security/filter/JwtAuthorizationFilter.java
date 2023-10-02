package com.kcy.fitapet.global.common.security.filter;

import com.kcy.fitapet.global.common.security.authentication.UserDetailServiceImpl;
import com.kcy.fitapet.global.common.util.cookie.CookieUtil;
import com.kcy.fitapet.global.common.util.jwt.JwtUtil;
import com.kcy.fitapet.global.common.util.jwt.entity.JwtUserInfo;
import com.kcy.fitapet.global.common.util.jwt.exception.AuthErrorCode;
import com.kcy.fitapet.global.common.util.jwt.exception.AuthErrorException;
import com.kcy.fitapet.global.common.util.redis.forbidden.ForbiddenTokenService;
import com.kcy.fitapet.global.common.util.redis.refresh.RefreshToken;
import com.kcy.fitapet.global.common.util.redis.refresh.RefreshTokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

import static com.kcy.fitapet.global.common.util.jwt.AuthConstants.*;

/**
 * 지정한 URL 별로 JWT 유효성 검증을 수행하며, 직접적인 사용자 인증을 확인합니다.
 */
@RequiredArgsConstructor
@Slf4j
public class JwtAuthorizationFilter extends OncePerRequestFilter {
    private final UserDetailServiceImpl userDetailServiceImpl;
    private final RefreshTokenService refreshTokenService;
    private final ForbiddenTokenService forbiddenTokenService;

    private final JwtUtil jwtUtil;
    private final CookieUtil cookieUtil;

    private final List<String> jwtIgnoreUrls = List.of(
            "/api/v1/members/register",
            "/api/v1/members/login",
            "/api/v1/members/refresh",
            "/api/v1/members/sms/**",
            "/v3/api-docs/**", "/swagger-ui/**", "/swagger",
            "/favicon.ico"
    );

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (shouldIgnoreRequest(request)) {
            log.info("Ignoring request: {}", request.getRequestURI());
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

    private String resolveAccessToken(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        String authHeader = request.getHeader(AUTH_HEADER.getValue());
        try {
            String token = jwtUtil.resolveToken(authHeader);

            if (forbiddenTokenService.isForbidden(token))
                throw new AuthErrorException(AuthErrorCode.FORBIDDEN_ACCESS_TOKEN, "더 이상 사용할 수 없는 토큰입니다.");

            Date expiryDate =  jwtUtil.getExpiryDate(token);
            return token;
        } catch (AuthErrorException e) {
            if (e.getErrorCode() == AuthErrorCode.EXPIRED_ACCESS_TOKEN) {
                log.warn("Expired JWT access token: {}", e.getMessage());
                return handleExpiredToken(request, response);
            }
            log.warn("Invalid JWT access token: {}", e.getMessage());
            throw new ServletException();
        }
    }

    private String handleExpiredToken(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        ResponseCookie cookie;
        try {
            cookie = cookieUtil.deleteCookie(request, response, REFRESH_TOKEN.getValue())
                    .orElseThrow(() -> new AuthErrorException(AuthErrorCode.REFRESH_TOKEN_NOT_FOUND, "Refresh token not found"));
        } catch (AuthErrorException e) {
            throw new ServletException(e);
        }

        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
        return reissueAccessToken(request, response);
    }

    private String reissueAccessToken(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        try {
            Cookie refreshTokenCookie = cookieUtil.getCookie(request, REFRESH_TOKEN.getValue())
                    .orElseThrow(() -> new AuthErrorException(AuthErrorCode.REFRESH_TOKEN_NOT_FOUND, "Refresh token not found"));
            String requestRefreshToken = refreshTokenCookie.getValue();

            RefreshToken reissuedRefreshToken = refreshTokenService.refresh(requestRefreshToken);
            ResponseCookie cookie = cookieUtil.createCookie(REFRESH_TOKEN.getValue(), reissuedRefreshToken.getToken(), refreshTokenCookie.getMaxAge());
            response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());

            JwtUserInfo userInfo = jwtUtil.getUserInfoFromToken(requestRefreshToken);
            String reissuedAccessToken = jwtUtil.generateAccessToken(userInfo);
            response.addHeader(ACCESS_TOKEN.getValue(), reissuedAccessToken);
            return reissuedAccessToken;
        } catch (AuthErrorException e) {
            throw new ServletException();
        }
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
}