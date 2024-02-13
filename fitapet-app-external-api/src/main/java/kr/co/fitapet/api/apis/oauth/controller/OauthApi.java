package kr.co.fitapet.api.apis.oauth.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import kr.co.fitapet.api.apis.auth.dto.SmsRes;
import kr.co.fitapet.api.apis.auth.usecase.MemberAuthUseCase;
import kr.co.fitapet.api.apis.oauth.dto.OauthSmsReq;
import kr.co.fitapet.api.apis.oauth.usecase.OauthUseCase;
import kr.co.fitapet.api.common.response.SuccessResponse;
import kr.co.fitapet.api.common.security.jwt.dto.Jwt;
import kr.co.fitapet.api.common.util.cookie.CookieUtil;
import kr.co.fitapet.domain.domains.oauth.dto.OauthSignInReq;
import kr.co.fitapet.domain.domains.oauth.dto.OauthSignUpReq;
import kr.co.fitapet.domain.domains.oauth.type.ProviderType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

import static kr.co.fitapet.api.common.security.jwt.consts.AuthConstants.*;

@Tag(name = "OAuth API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth/oauth")
@Slf4j
public class OauthApi {
    private final OauthUseCase oauthUseCase;
    private final CookieUtil cookieUtil;

    @Operation(summary = "OAuth 로그인")
    @Parameter(name = "provider", description = "OAuth 제공자", required = true, in = ParameterIn.QUERY)
    @PostMapping("")
    @PreAuthorize("isAnonymous()")
    public ResponseEntity<?> signIn(
            @RequestParam("provider") ProviderType provider,
            @RequestBody @Valid OauthSignInReq req
    ) {
        Optional<Pair<Long, Jwt>> jwt;
        if (ProviderType.NAVER.equals(provider)) {
            return null; // TODO: 2023-12-24 네이버 로그인 구현
        } else {
            jwt = oauthUseCase.signInByOIDC(req.id(), req.idToken(), provider, req.nonce());
        }

        return jwt.isPresent()
                ? getJwtResponseEntity(jwt.get().getKey(), jwt.get().getValue())
                : ResponseEntity.ok(SuccessResponse.from(Map.of("id", req.id())));
    }

    @Operation(summary = "OAuth 회원가입", description = "/{id}/sms로 전화번호 인증 후, accessToken 발급이 선행되어야 한다.")
    @Parameters({
            @Parameter(name = "id", description = "OAuth 제공자에서 발급받은 ID", required = true, in = ParameterIn.PATH),
            @Parameter(name = "provider", description = "OAuth 제공자", required = true, in = ParameterIn.QUERY),
            @Parameter(name = "accessToken", description = "OAuth 전화번호 인증 시 발급받은 accessToken", required = true, in = ParameterIn.HEADER),
    })
    @PostMapping("/{id}")
    @PreAuthorize("isAnonymous()")
    public ResponseEntity<?> signUp(
            @PathVariable("id") String id,
            @RequestParam("provider") ProviderType provider,
            @RequestHeader("Authorization") String accessToken,
            @RequestBody @Valid OauthSignUpReq req
    ) {
        Pair<Long, Jwt> jwt;
        if (ProviderType.NAVER.equals(provider)) {
            return null; // TODO: 2023-12-24 네이버 로그인 구현
        } else {
            jwt = oauthUseCase.signUpByOIDC(id, provider, accessToken, req);
        }

        return getJwtResponseEntity(jwt.getKey(), jwt.getValue());
    }

    @Operation(summary = "OAuth 회원가입 전화번호 인증")
    @Parameters({
            @Parameter(name = "id", description = "OAuth 제공자에서 발급받은 ID", required = true, in = ParameterIn.PATH),
            @Parameter(name = "provider", description = "OAuth 제공자", required = true, in = ParameterIn.QUERY),
            @Parameter(name = "code", description = "인증번호", in = ParameterIn.QUERY),
    })
    @PostMapping("/{id}/sms")
    @PreAuthorize("isAnonymous()")
    public ResponseEntity<?> signUpSmsAuthorization(
        @PathVariable("id") String id,
        @RequestParam("provider") ProviderType provider,
        @RequestParam(value = "code", required = false) String code,
        @RequestBody @Valid OauthSmsReq req
    ) {
        if (code == null) {
            SmsRes smsRes = oauthUseCase.sendCode(req, provider);
            return ResponseEntity.ok(SuccessResponse.from(smsRes));
        }

        Pair<Long, Jwt> token = oauthUseCase.checkCertificationNumber(req, id, code, provider);
        if (token.getValue() == null)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        else if (token.getValue().refreshToken() == null)
            return ResponseEntity.ok()
                    .header(AUTH_HEADER.getValue(), token.getValue().accessToken())
                    .body(SuccessResponse.from(Map.of("member", "신규 회원")));

        return getJwtResponseEntity(token.getKey(), token.getValue());
    }

    private ResponseEntity<?> getJwtResponseEntity(Long userId, Jwt jwt) {
        ResponseCookie cookie = cookieUtil.createCookie(REFRESH_TOKEN.getValue(), jwt.refreshToken(), 60 * 60 * 24 * 7);

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .header(AUTH_HEADER.getValue(), jwt.accessToken())
                .body(SuccessResponse.from(Map.of("userId", userId)));
    }
}
