package com.kcy.fitapet.global.config;

import com.kcy.fitapet.global.common.security.authentication.UserDetailServiceImpl;
import com.kcy.fitapet.global.common.security.filter.JwtAuthorizationFilter;
import com.kcy.fitapet.global.common.security.filter.JwtExceptionFilter;
import com.kcy.fitapet.global.common.util.cookie.CookieUtil;
import com.kcy.fitapet.global.common.util.jwt.JwtUtil;
import com.kcy.fitapet.global.common.util.redis.forbidden.ForbiddenTokenService;
import com.kcy.fitapet.global.common.util.redis.refresh.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
public class JwtSecurityConfig extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {
    private final UserDetailServiceImpl userDetailServiceImpl;
    private final RefreshTokenService refreshTokenService;
    private final ForbiddenTokenService forbiddenTokenService;

    private final JwtUtil jwtUtil;
    private final CookieUtil cookieUtil;

    @Override
    public void configure(HttpSecurity http) throws Exception {
        JwtAuthorizationFilter jwtAuthorizationFilter
                = new JwtAuthorizationFilter(userDetailServiceImpl, refreshTokenService, forbiddenTokenService, jwtUtil, cookieUtil);
        JwtExceptionFilter jwtExceptionFilter = new JwtExceptionFilter();

        // TODO: test
        http.addFilterBefore(jwtAuthorizationFilter, UsernamePasswordAuthenticationFilter.class);
        http.addFilterBefore(jwtExceptionFilter, JwtAuthorizationFilter.class);
    }
}