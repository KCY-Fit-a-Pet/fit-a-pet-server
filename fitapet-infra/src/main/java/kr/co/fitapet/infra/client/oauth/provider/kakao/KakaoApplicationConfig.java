package kr.co.fitapet.infra.client.oauth.provider.kakao;

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
@ConfigurationProperties(prefix = "oauth2.client.provider.kakao")
@ConfigurationPropertiesBinding
public class KakaoApplicationConfig implements OauthApplicationConfig {
    private String jwksUri;
    private String clientId;
    private String clientSecret;
    private String clientName;
}
