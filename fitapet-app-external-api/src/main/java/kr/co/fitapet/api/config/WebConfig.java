package kr.co.fitapet.api.config;

import kr.co.fitapet.api.common.converter.MemberAttrTypeConverter;
import kr.co.fitapet.api.common.converter.NotificationTypeQueryConverter;
import kr.co.fitapet.api.common.converter.ProviderTypeQueryConverter;
import kr.co.fitapet.api.common.converter.SmsPrefixConverter;
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
        registrar.addConverter(new ProviderTypeQueryConverter());
    }
}
