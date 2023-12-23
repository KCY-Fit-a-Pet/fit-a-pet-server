package com.kcy.fitapet.global.common.security.oauth.kakao;

import feign.Response;
import feign.codec.ErrorDecoder;

public class KauthErrorDecoder implements ErrorDecoder {
    @Override
    public Exception decode(String methodKey, Response response) {
        return null;
    }
}
