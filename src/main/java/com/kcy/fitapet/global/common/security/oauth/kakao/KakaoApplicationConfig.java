package com.kcy.fitapet.global.common.security.oauth.kakao;

import com.kcy.fitapet.global.common.security.oauth.OauthApplicationConfig;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConfigurationPropertiesBinding;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Getter
@Setter
@Validated
@Component
@ConfigurationProperties(prefix = "oauth2.client.provider.kakao")
@ConfigurationPropertiesBinding
public class KakaoApplicationConfig implements OauthApplicationConfig {
    @NotEmpty
    private String jwksUri;
    @NotEmpty
    private String clientId;
    @NotEmpty
    private String clientSecret;
    @NotEmpty
    private String clientName;
}
