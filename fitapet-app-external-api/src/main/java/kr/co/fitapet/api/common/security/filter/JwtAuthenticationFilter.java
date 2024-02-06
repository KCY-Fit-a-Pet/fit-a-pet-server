package kr.co.fitapet.api.common.security.filter;


import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;
import kr.co.fitapet.api.common.security.authentication.UserDetailServiceImpl;
import kr.co.fitapet.api.common.security.jwt.JwtProvider;
import kr.co.fitapet.api.common.security.jwt.exception.AuthErrorCode;
import kr.co.fitapet.api.common.security.jwt.exception.AuthErrorException;
import kr.co.fitapet.domain.common.redis.forbidden.ForbiddenTokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.regex.Pattern;

import static kr.co.fitapet.api.common.security.jwt.AuthConstants.AUTH_HEADER;
import static kr.co.fitapet.api.common.security.jwt.AuthConstants.REFRESH_TOKEN;

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
    private final ForbiddenTokenService forbiddenTokenService;

    private final JwtProvider accessTokenProvider;

    /**
     * JWT 인증 필터에서 무시할 URL 패턴 <br/>
     * ex. SMS 인증을 위한 jwt를 사용하는 경우
     */
    private List<String> jwtIgnoreUrls = List.of(
            "/api/v1/auth/register",
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

        String token = accessTokenProvider.resolveToken(authHeader);
        if (!StringUtils.hasText(token))
            handleAuthException(AuthErrorCode.EMPTY_ACCESS_TOKEN, "액세스 토큰이 없습니다.");

        if (forbiddenTokenService.isForbidden(token))
            handleAuthException(AuthErrorCode.FORBIDDEN_ACCESS_TOKEN, "더 이상 사용할 수 없는 토큰입니다.");

        accessTokenProvider.isTokenExpired(token);
        return token;
    }

    private UserDetails getUserDetails(final String accessToken) {
        Long userId = accessTokenProvider.getSubInfoFromToken(accessToken).id();
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