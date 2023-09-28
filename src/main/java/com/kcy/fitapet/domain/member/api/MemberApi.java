package com.kcy.fitapet.domain.member.api;

import com.kcy.fitapet.domain.member.dto.SignInReq;
import com.kcy.fitapet.domain.member.dto.SignUpReq;
import com.kcy.fitapet.domain.member.service.component.MemberAuthService;
import com.kcy.fitapet.global.common.security.authentication.CustomUserDetails;
import com.kcy.fitapet.global.common.util.cookie.CookieUtil;
import com.kcy.fitapet.global.common.util.jwt.entity.JwtUserInfo;
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
    public ResponseEntity<?> register(@RequestBody @Valid SignUpReq dto) {
        Map<String, String> tokens = memberAuthService.register(dto);
        return getResponseEntity(tokens);
    }

    @GetMapping("/sms")
    public ResponseEntity<?> smsTest(@RequestParam String phoneNumber, @RequestParam String code) {

        return ResponseEntity.noContent().build();
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid SignInReq dto) {
        Map<String, String> tokens = memberAuthService.login(dto);
        return getResponseEntity(tokens);
    }

    @GetMapping("/logout")
    public ResponseEntity<?> logout(@CookieValue("refreshToken") String refreshToken, HttpServletRequest request, HttpServletResponse response) {
        memberAuthService.logout(request.getHeader(AUTH_HEADER.getValue()), refreshToken);
        ResponseCookie cookie = cookieUtil.deleteCookie(request, response, REFRESH_TOKEN.getValue())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 쿠키입니다."));

        return ResponseEntity.noContent().header(HttpHeaders.SET_COOKIE, cookie.toString()).build();
    }

    @GetMapping("/refresh")
    public ResponseEntity<?> refresh(@CookieValue("refreshToken") String refreshToken) {
        if (refreshToken == null) {
            throw new IllegalArgumentException("존재하지 않는 쿠키입니다."); // TODO : 공통 예외로 변경
        }
        Map<String, String> tokens = memberAuthService.refresh(refreshToken);
        ResponseCookie cookie = cookieUtil.createCookie(REFRESH_TOKEN.getValue(), tokens.get(REFRESH_TOKEN.getValue()), 60 * 60 * 24 * 7);

        return ResponseEntity.noContent()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .header(ACCESS_TOKEN.getValue(), tokens.get(ACCESS_TOKEN.getValue()))
                .build();
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

        return ResponseEntity.noContent()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .header(ACCESS_TOKEN.getValue(), tokens.get(ACCESS_TOKEN.getValue()))
                .build();
    }
}
