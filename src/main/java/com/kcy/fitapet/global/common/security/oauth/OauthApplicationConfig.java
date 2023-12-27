package com.kcy.fitapet.global.common.security.oauth;

import java.util.List;

public interface OauthApplicationConfig {
    String getJwksUri();
    String getClientId();
    String getClientSecret();
    String getClientName();
}
