package kr.co.fitapet.infra.client.oauth.environment;

public interface OauthApplicationConfig {
    String getJwksUri();
    String getClientId();
    String getClientSecret();
    String getClientName();
}
