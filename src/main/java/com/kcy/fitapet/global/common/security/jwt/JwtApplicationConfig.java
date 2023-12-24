package com.kcy.fitapet.global.common.security.jwt;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConfigurationPropertiesBinding;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import java.time.Duration;

@Getter
@Setter
@Validated
@Component
@ConfigurationProperties(prefix = "jwt.token")
@ConfigurationPropertiesBinding
public class JwtApplicationConfig {
    Duration accessExpirationTime;
    Duration refreshExpirationTime;
    Duration smsAuthExpirationTime;
}
