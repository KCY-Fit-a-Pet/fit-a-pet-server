package kr.co.fitapet.infra.client.oauth.provider.google;

import kr.co.fitapet.infra.client.oauth.environment.OauthApplicationConfig;
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
    private String authUri;
    private String clientId;
    private String clientSecret;
    private String clientName;

    @Override
    public String getJwksUri() {
        return authUri;
    }

    public void setJwksUri(String jwksUri) {

    }
}