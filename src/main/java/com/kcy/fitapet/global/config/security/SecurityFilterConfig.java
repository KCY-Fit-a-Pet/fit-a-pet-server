package com.kcy.fitapet.global.config.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kcy.fitapet.global.common.redis.forbidden.ForbiddenTokenService;
import com.kcy.fitapet.global.common.redis.refresh.RefreshTokenService;
import com.kcy.fitapet.global.common.security.authentication.UserDetailServiceImpl;
import com.kcy.fitapet.global.common.security.filter.JwtAuthenticationFilter;
import com.kcy.fitapet.global.common.security.filter.JwtExceptionFilter;
import com.kcy.fitapet.global.common.security.jwt.JwtProvider;
import com.kcy.fitapet.global.common.security.jwt.JwtProviderMapper;
import com.kcy.fitapet.global.common.util.cookie.CookieUtil;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@Configuration
public class SecurityFilterConfig {
    private final UserDetailServiceImpl userDetailServiceImpl;
    private final ForbiddenTokenService forbiddenTokenService;

    private final JwtProvider accessTokenProvider;

    private final ObjectMapper objectMapper;

    @Bean
    public JwtExceptionFilter jwtExceptionFilter() {
        return new JwtExceptionFilter(objectMapper);
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthorizationFilter() {
        return new JwtAuthenticationFilter(userDetailServiceImpl, forbiddenTokenService, accessTokenProvider);
    }
}
