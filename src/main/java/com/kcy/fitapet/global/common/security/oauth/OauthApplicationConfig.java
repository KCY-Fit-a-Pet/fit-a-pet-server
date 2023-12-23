package com.kcy.fitapet.global.common.security.oauth;

import java.util.List;

public interface OauthApplicationConfig {
    String getAuthorizationUri();
    String getClientId();
    String getClientSecret();
    String getClientName();
    List<String> getScopes();
}
