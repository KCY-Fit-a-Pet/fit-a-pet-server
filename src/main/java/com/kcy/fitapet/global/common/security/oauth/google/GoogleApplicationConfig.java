package com.kcy.fitapet.global.common.security.oauth.google;

import com.kcy.fitapet.global.common.security.oauth.OauthApplicationConfig;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConfigurationPropertiesBinding;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

@Getter
@Setter
@Validated
@Component
@ConfigurationProperties(prefix = "oauth2.client.provider.google")
@ConfigurationPropertiesBinding
public class GoogleApplicationConfig implements OauthApplicationConfig {
    @NotEmpty
    private String jwksUri;
    @NotEmpty
    private String clientId;
    @NotEmpty
    private String clientSecret;
    @NotEmpty
    private String clientName;
}