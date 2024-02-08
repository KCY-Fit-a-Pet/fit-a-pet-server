package kr.co.fitapet.infra.config.feign;

import feign.codec.Encoder;
import feign.form.FormEncoder;
import feign.okhttp.OkHttpClient;
import org.springframework.context.annotation.Bean;

public class KakaoOauthConfig {
    @Bean
    public OkHttpClient client() {
        return new OkHttpClient();
    }

    @Bean
    Encoder formEncoder() {
        return new FormEncoder();
    }
}
