package com.kcy.fitapet.global.config;

import com.kcy.fitapet.domain.member.type.converter.MemberAttrTypeConverter;
import com.kcy.fitapet.domain.notification.type.NotificationTypeQueryConverter;
import com.kcy.fitapet.global.common.redis.sms.SmsPrefixConverter;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addFormatters(FormatterRegistry registrar) {

        registrar.addConverter(new SmsPrefixConverter());
        registrar.addConverter(new NotificationTypeQueryConverter());
        registrar.addConverter(new MemberAttrTypeConverter());
    }
}
