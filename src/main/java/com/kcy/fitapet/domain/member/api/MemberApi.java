package com.kcy.fitapet.domain.member.api;

import com.kcy.fitapet.domain.member.dto.auth.SignInReq;
import com.kcy.fitapet.domain.member.dto.auth.SignUpReq;
import com.kcy.fitapet.domain.member.dto.sms.SmsReq;
import com.kcy.fitapet.domain.member.dto.sms.SmsRes;
import com.kcy.fitapet.domain.member.service.component.MemberAuthService;
import com.kcy.fitapet.global.common.response.FailureResponse;
import com.kcy.fitapet.global.common.response.SuccessResponse;
import com.kcy.fitapet.global.common.response.code.ErrorCode;
import com.kcy.fitapet.global.common.security.authentication.CustomUserDetails;
import com.kcy.fitapet.global.common.util.cookie.CookieUtil;
import com.kcy.fitapet.global.common.util.jwt.entity.JwtUserInfo;
import com.kcy.fitapet.global.common.util.jwt.exception.AuthErrorCode;
import com.kcy.fitapet.global.common.util.jwt.exception.AuthErrorException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

import static com.kcy.fitapet.global.common.util.jwt.AuthConstants.*;

@RestController
@RequestMapping("/api/v1/members")
@RequiredArgsConstructor
@Slf4j
public class MemberApi {
    private final MemberAuthService memberAuthService;
    private final CookieUtil cookieUtil;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestHeader String accessToken, @RequestBody @Valid SignUpReq dto) {
        Map<String, String> tokens = memberAuthService.register(accessToken, dto);
        return getResponseEntity(tokens);
    }

    @GetMapping("/sms")
    public ResponseEntity<?> smsAuthorization(
            @RequestParam(value = "phone") String phoneNumber,
            @RequestParam(value = "code", required = false) String code) {
        if (code == null) {
            SmsRes smsRes = memberAuthService.sendCertificationNumber(new SmsReq(phoneNumber));
            return ResponseEntity.ok(SuccessResponse.from(smsRes));
        }

        String token = memberAuthService.checkCertificationNumber(phoneNumber, code);

        return (token.equals(""))
                ? ResponseEntity.ok(FailureResponse.of("code", ErrorCode.INVALID_AUTH_CODE.getMessage()))
                : ResponseEntity.ok()
                    .header(ACCESS_TOKEN.getValue(), token)
                    .body(SuccessResponse.from(Map.of("code", "인증 성공")));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid SignInReq dto) {
        Map<String, String> tokens = memberAuthService.login(dto);
        return getResponseEntity(tokens);
    }

    @GetMapping("/logout")
    public ResponseEntity<?> logout(@CookieValue("refreshToken") @Valid String refreshToken, HttpServletRequest request, HttpServletResponse response) {
        memberAuthService.logout(request.getHeader(AUTH_HEADER.getValue()), refreshToken);
        ResponseCookie cookie = cookieUtil.deleteCookie(request, response, REFRESH_TOKEN.getValue())
                .orElseThrow(() -> new AuthErrorException(AuthErrorCode.REFRESH_TOKEN_NOT_FOUND, "존재하지 않는 쿠키입니다."));

        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString()).body(SuccessResponse.noContent());
    }

    @GetMapping("/refresh")
    public ResponseEntity<?> refresh(@CookieValue("refreshToken") @Valid String refreshToken) {
        if (refreshToken == null) {
            throw new AuthErrorException(AuthErrorCode.REFRESH_TOKEN_NOT_FOUND, "존재하지 않는 쿠키입니다.");
        }
        Map<String, String> tokens = memberAuthService.refresh(refreshToken);

        return getResponseEntity(tokens);
    }

    @GetMapping("/authentication")
    public ResponseEntity<?> authenticationTest(@AuthenticationPrincipal CustomUserDetails securityUser, Authentication authentication) {
        log.info("type: {}", authentication.getPrincipal());
        JwtUserInfo user = securityUser.toJwtUserInfo();
        log.info("user: {}", user);

        return ResponseEntity.ok(user);
    }

    private ResponseEntity<?> getResponseEntity(Map<String, String> tokens) {
        log.debug("access token: {}", tokens.get(ACCESS_TOKEN.getValue()));
        log.debug("refresh token: {}", tokens.get(REFRESH_TOKEN.getValue()));
        ResponseCookie cookie = cookieUtil.createCookie(REFRESH_TOKEN.getValue(), tokens.get(REFRESH_TOKEN.getValue()), 60 * 60 * 24 * 7);

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .header(ACCESS_TOKEN.getValue(), tokens.get(ACCESS_TOKEN.getValue()))
                .body(SuccessResponse.noContent());
    }
}
