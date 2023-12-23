package com.kcy.fitapet.global.config.feign;

import com.kcy.fitapet.global.common.security.oauth.kakao.KauthErrorDecoder;
import feign.codec.Encoder;
import feign.codec.ErrorDecoder;
import feign.form.FormEncoder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

@Import(KauthErrorDecoder.class)
public class KakaoOauthConfig {
    @Bean
    @ConditionalOnMissingBean(value = ErrorDecoder.class)
    public KauthErrorDecoder commonFeignErrorDecoder() {
        return new KauthErrorDecoder();
    }

    @Bean
    Encoder formEncoder() {
        return new FormEncoder();
    }
}
