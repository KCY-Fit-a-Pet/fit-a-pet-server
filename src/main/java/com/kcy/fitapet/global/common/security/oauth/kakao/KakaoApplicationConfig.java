package com.kcy.fitapet.global.common.security.oauth.kakao;

import com.kcy.fitapet.global.common.security.oauth.OauthApplicationConfig;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConfigurationPropertiesBinding;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Getter
@Validated
@Component
@ConfigurationProperties(prefix = "kakao")
@ConfigurationPropertiesBinding
public class KakaoApplicationConfig implements OauthApplicationConfig {
    @NotEmpty
    private String authorizationUri;
    @NotEmpty
    private String clientId;
    @NotEmpty
    private String clientSecret;
    @NotEmpty
    private String clientName;
    private List<String> scopes;
}
