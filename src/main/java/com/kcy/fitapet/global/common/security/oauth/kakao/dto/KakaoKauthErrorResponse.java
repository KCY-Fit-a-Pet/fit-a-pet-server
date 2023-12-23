package com.kcy.fitapet.global.common.security.oauth.kakao.dto;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.kcy.fitapet.global.common.response.code.ErrorCode;
import com.kcy.fitapet.global.common.response.exception.GlobalErrorException;
import feign.Response;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.IOException;
import java.io.InputStream;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class KakaoKauthErrorResponse {
    private String error;
    private String errorCode;
    private String errorDescription;

    public static KakaoKauthErrorResponse from(Response response) {
        try (InputStream bodyIs = response.body().asInputStream()) {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(bodyIs, KakaoKauthErrorResponse.class);
        } catch (IOException e) {
            throw new GlobalErrorException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }
}
