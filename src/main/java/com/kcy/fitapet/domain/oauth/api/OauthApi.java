package com.kcy.fitapet.domain.oauth.api;

import com.kcy.fitapet.domain.oauth.dto.OauthSignInReq;
import com.kcy.fitapet.domain.oauth.dto.OauthSignUpReq;
import com.kcy.fitapet.domain.oauth.service.component.OauthService;
import com.kcy.fitapet.domain.oauth.type.ProviderType;
import com.kcy.fitapet.global.common.response.SuccessResponse;
import com.kcy.fitapet.global.common.security.jwt.dto.Jwt;
import com.kcy.fitapet.global.common.util.cookie.CookieUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.hc.core5.http.HttpStatus;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

import static com.kcy.fitapet.global.common.security.jwt.AuthConstants.*;
import static com.kcy.fitapet.global.common.security.jwt.AuthConstants.ACCESS_TOKEN;

@Tag(name = "OAuth API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth/oauth")
@Slf4j
public class OauthApi {
    private final OauthService oAuthService;
    private final CookieUtil cookieUtil;

    @Operation(summary = "OAuth 로그인")
    @Parameters({
            @Parameter(name = "provider", description = "OAuth 제공자"),
            @Parameter(name = "req", description = "OAuth 로그인 요청 정보")
    })
    @PostMapping("")
    @PreAuthorize("isAnonymous()")
    public ResponseEntity<?> signIn(
            @RequestParam("provider") ProviderType provider,
            @RequestBody @Valid OauthSignInReq req
    ) {
        Jwt jwt = null;
        if (ProviderType.NAVER.equals(provider)) {
            return null; // TODO: 2023-12-24 네이버 로그인 구현
        } else {
            jwt = oAuthService.signInByOIDC(req.id(), req.id_token(), provider, req.nonce());
        }

        return (jwt == null)
                ? ResponseEntity.ok(SuccessResponse.from(Map.of("id", req.id())))
                : getResponseEntity(jwt);
    }

    @PostMapping("/{id}")
    @PreAuthorize("isAnonymous()")
    public ResponseEntity<?> signUp(
            @PathVariable("id") Long id,
            @RequestParam("provider") ProviderType provider,
            @RequestBody @Valid OauthSignUpReq req
    ) {
        Jwt jwt = null;
        if (ProviderType.NAVER.equals(provider)) {
            return null; // TODO: 2023-12-24 네이버 로그인 구현
        } else {
            jwt = oAuthService.signUpByOIDC(id, provider, req);
        }

        return getResponseEntity(jwt);
    }

    private ResponseEntity<?> getResponseEntity(Jwt jwt) {
        ResponseCookie cookie = cookieUtil.createCookie(REFRESH_TOKEN.getValue(), jwt.refreshToken(), 60 * 60 * 24 * 7);

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .header(ACCESS_TOKEN.getValue(), jwt.accessToken())
                .body(SuccessResponse.noContent());
    }
}
