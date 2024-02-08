package kr.co.fitapet.api.config.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.co.fitapet.api.common.security.authentication.UserDetailServiceImpl;
import kr.co.fitapet.api.common.security.filter.JwtAuthenticationFilter;
import kr.co.fitapet.api.common.security.filter.JwtExceptionFilter;
import kr.co.fitapet.api.common.security.jwt.JwtProvider;
import kr.co.fitapet.domain.common.redis.forbidden.ForbiddenTokenService;
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
