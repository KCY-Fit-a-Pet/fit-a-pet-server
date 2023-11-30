package com.kcy.fitapet.global.config;

import com.kcy.fitapet.global.common.util.redis.sms.SmsPrefixConverter;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addFormatters(FormatterRegistry registrar) {
        registrar.addConverter(new SmsPrefixConverter());
    }
}
