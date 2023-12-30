package com.kcy.fitapet.domain.oauth.api;

import com.kcy.fitapet.domain.member.service.component.MemberAuthService;
import com.kcy.fitapet.domain.oauth.dto.OauthSignInReq;
import com.kcy.fitapet.domain.oauth.dto.OauthSignUpReq;
import com.kcy.fitapet.domain.oauth.dto.OauthSmsReq;
import com.kcy.fitapet.domain.oauth.service.component.OauthService;
import com.kcy.fitapet.domain.oauth.type.ProviderType;
import com.kcy.fitapet.global.common.response.SuccessResponse;
import com.kcy.fitapet.global.common.security.jwt.dto.Jwt;
import com.kcy.fitapet.global.common.util.cookie.CookieUtil;
import com.kcy.fitapet.global.common.util.sms.dto.SmsRes;
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
import java.util.Optional;

import static com.kcy.fitapet.global.common.security.jwt.AuthConstants.ACCESS_TOKEN;
import static com.kcy.fitapet.global.common.security.jwt.AuthConstants.REFRESH_TOKEN;

@Tag(name = "OAuth API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth/oauth")
@Slf4j
public class OauthApi {
    private final MemberAuthService memberAuthService;
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
        Optional<Jwt> jwt;
        if (ProviderType.NAVER.equals(provider)) {
            return null; // TODO: 2023-12-24 네이버 로그인 구현
        } else {
            jwt = oAuthService.signInByOIDC(req.id(), req.idToken(), provider, req.nonce());
        }

        return jwt.isPresent()
                ? getJwtResponseEntity(jwt.get())
                : ResponseEntity.ok(SuccessResponse.from(Map.of("id", req.id())));
    }

    @Operation(summary = "OAuth 회원가입", description = "/{id}/sms로 전화번호 인증 후, accessToken 발급이 선행되어야 한다.")
    @Parameters({
            @Parameter(name = "id", description = "OAuth 제공자에서 발급받은 ID"),
            @Parameter(name = "provider", description = "OAuth 제공자"),
            @Parameter(name = "accessToken", description = "OAuth 전화번호 인증 시 발급받은 accessToken"),
            @Parameter(name = "req", description = "OAuth 회원가입 요청 정보")
    })
    @PostMapping("/{id}")
    @PreAuthorize("isAnonymous()")
    public ResponseEntity<?> signUp(
            @PathVariable("id") Long id,
            @RequestParam("provider") ProviderType provider,
            @RequestHeader("Authorization") String accessToken,
            @RequestBody @Valid OauthSignUpReq req
    ) {
        Jwt jwt;
        if (ProviderType.NAVER.equals(provider)) {
            return null; // TODO: 2023-12-24 네이버 로그인 구현
        } else {
            jwt = oAuthService.signUpByOIDC(id, provider, accessToken, req);
        }

        return getJwtResponseEntity(jwt);
    }

    @Operation(summary = "OAuth 회원가입 전화번호 인증")
    @Parameters({
            @Parameter(name = "id", description = "OAuth 제공자에서 발급받은 ID"),
            @Parameter(name = "provider", description = "OAuth 제공자"),
            @Parameter(name = "req", description = "OAuth 회원가입 전화번호 인증 요청 정보")
    })
    @PostMapping("/{id}/sms")
    @PreAuthorize("isAnonymous()")
    public ResponseEntity<?> signUpSmsAuthorization(
        @PathVariable("id") Long id,
        @RequestParam("provider") ProviderType provider,
        @RequestParam(value = "code", required = false) String code,
        @RequestBody @Valid OauthSmsReq req
    ) {
        if (code == null) {
            SmsRes smsRes = oAuthService.sendCode(req, provider);
            return ResponseEntity.ok(SuccessResponse.from(smsRes));
        }

        Jwt token = oAuthService.checkCertificationNumber(req, id, code, provider);
        if (token == null)
            return ResponseEntity.status(HttpStatus.SC_UNAUTHORIZED).build();
        else if (token.refreshToken() == null)
            return ResponseEntity.ok()
                    .header(ACCESS_TOKEN.getValue(), token.accessToken())
                    .body(SuccessResponse.from(Map.of("member", "신규 회원")));

        return getJwtResponseEntity(token);
    }

    private ResponseEntity<?> getJwtResponseEntity(Jwt jwt) {
        ResponseCookie cookie = cookieUtil.createCookie(REFRESH_TOKEN.getValue(), jwt.refreshToken(), 60 * 60 * 24 * 7);

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .header(ACCESS_TOKEN.getValue(), jwt.accessToken())
                .body(SuccessResponse.noContent());
    }
}
