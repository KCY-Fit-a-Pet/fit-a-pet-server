package com.kcy.fitapet.global.config.feign;

import feign.codec.Encoder;
import feign.form.FormEncoder;
import org.springframework.context.annotation.Bean;

public class AppleOauthConfig {
    @Bean
    Encoder formEncoder() {
        return new FormEncoder();
    }
}
