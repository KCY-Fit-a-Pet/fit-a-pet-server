package com.kcy.fitapet.global.config;

import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignAutoConfiguration;
import org.springframework.cloud.openfeign.clientconfig.HttpClient5FeignConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableFeignClients(basePackages = "com.kcy.fitapet")
@ImportAutoConfiguration({FeignAutoConfiguration.class, HttpClient5FeignConfiguration.class})
public class FeignConfig {
}
