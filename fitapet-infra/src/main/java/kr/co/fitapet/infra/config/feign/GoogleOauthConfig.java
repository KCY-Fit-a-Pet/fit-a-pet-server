package kr.co.fitapet.infra.config.feign;

import feign.codec.Encoder;
import feign.form.FormEncoder;
import org.springframework.context.annotation.Bean;

public class GoogleOauthConfig {
    @Bean
    Encoder formEncoder() {
        return new FormEncoder();
    }
}
