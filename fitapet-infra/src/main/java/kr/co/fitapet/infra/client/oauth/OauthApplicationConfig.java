package kr.co.fitapet.infra.client.oauth;

public interface OauthApplicationConfig {
    String getJwksUri();
    String getClientId();
    String getClientSecret();
    String getClientName();
}
