package com.kcy.fitapet.global.config;

import com.kcy.fitapet.global.common.resolver.access.AccessTokenInfoResolver;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@Configuration
public class ArgumentResolverConfig implements WebMvcConfigurer {
        private final AccessTokenInfoResolver accessTokenInfoResolver;

        @Override
        public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
            resolvers.add(accessTokenInfoResolver);
        }
}
