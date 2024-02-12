package kr.co.fitapet.infra.client.oauth.provider.apple;

import kr.co.fitapet.infra.client.oauth.OauthApplicationConfig;
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
@ConfigurationProperties(prefix = "oauth2.client.provider.apple")
@ConfigurationPropertiesBinding
public class AppleApplicationConfig implements OauthApplicationConfig {
    private String jwksUri;
    private String clientId;
    private String clientSecret;
    private String clientName;
}
