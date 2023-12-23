package com.kcy.fitapet.domain.member.api;

import com.kcy.fitapet.domain.member.dto.auth.SignInReq;
import com.kcy.fitapet.domain.member.dto.auth.SignUpReq;
import com.kcy.fitapet.global.common.redis.sms.SmsPrefix;
import com.kcy.fitapet.global.common.util.sms.dto.SmsReq;
import com.kcy.fitapet.global.common.util.sms.dto.SmsRes;
import com.kcy.fitapet.domain.member.exception.SmsErrorCode;
import com.kcy.fitapet.domain.member.service.component.MemberAuthService;
import com.kcy.fitapet.global.common.resolver.access.AccessToken;
import com.kcy.fitapet.global.common.resolver.access.AccessTokenInfo;
import com.kcy.fitapet.global.common.response.ErrorResponse;
import com.kcy.fitapet.global.common.response.FailureResponse;
import com.kcy.fitapet.global.common.response.SuccessResponse;
import com.kcy.fitapet.global.common.response.code.ErrorCode;
import com.kcy.fitapet.global.common.response.exception.GlobalErrorException;
import com.kcy.fitapet.global.common.util.cookie.CookieUtil;
import com.kcy.fitapet.global.common.security.jwt.AuthConstants;
import com.kcy.fitapet.global.common.security.jwt.exception.AuthErrorCode;
import com.kcy.fitapet.global.common.security.jwt.exception.AuthErrorException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

import static com.kcy.fitapet.global.common.security.jwt.AuthConstants.ACCESS_TOKEN;
import static com.kcy.fitapet.global.common.security.jwt.AuthConstants.REFRESH_TOKEN;

@Tag(name = "유저 관리 API", description = "유저 인증과 관련된 API")
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthApi {
    private final MemberAuthService memberAuthService;
    private final CookieUtil cookieUtil;

    @Operation(summary = "회원가입", description = "유저 닉네임, 패스워드를 입력받고 유효하다면 액세스 토큰(헤더)과 리프레시 토큰(쿠키)을 반환합니다.")
    @Parameters({
            @Parameter(name = "Authorization", description = "전화번호 인증 후 받은 토큰", in = ParameterIn.HEADER),
            @Parameter(name = "dto", description = "회원가입 정보", schema = @Schema(implementation = SignUpReq.class))
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "회원가입 성공", content = @Content(schema = @Schema(implementation = SuccessResponse.class))),
            @ApiResponse(responseCode = "400", description = "회원가입 실패", content = @Content(schema = @Schema(implementation = FailureResponse.class))),
            @ApiResponse(responseCode = "4xx", description = "에러", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
    })
    @PostMapping("/register")
    @PreAuthorize("isAnonymous()")
    public ResponseEntity<?> signUp(@RequestHeader("Authorization") @NotBlank String accessToken, @RequestBody @Valid SignUpReq dto) {
        Map<String, String> tokens = memberAuthService.register(accessToken, dto);
        return getResponseEntity(tokens);
    }

    @Operation(summary = "회원가입 전화번호 인증")
    @Parameters({
            @Parameter(name = "phone", description = "전화번호", in = ParameterIn.QUERY, required = true),
            @Parameter(name = "code", description = "인증번호", in = ParameterIn.QUERY)
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "인증번호 전송 성공", content = @Content(schema = @Schema(implementation = SuccessResponse.class))),
            @ApiResponse(responseCode = "200", description = "인증번호 전송 실패", content = @Content(schema = @Schema(implementation = FailureResponse.class))),
            @ApiResponse(responseCode = "4xx", description = "에러", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
    })
    @PostMapping("/register-sms")
    @PreAuthorize("isAnonymous()")
    public ResponseEntity<?> registerSmsAuthorization(
            @RequestParam(value = "code", required = false) String code,
            @RequestBody @Valid SmsReq dto) {
        if (code == null) {
            SmsRes smsRes = memberAuthService.sendCertificationNumber(dto, SmsPrefix.REGISTER);
            return ResponseEntity.ok(SuccessResponse.from(smsRes));
        }

        String token = memberAuthService.checkCertificationForRegister(dto, code);
        if (!StringUtils.hasText(token))
            throw new GlobalErrorException(SmsErrorCode.INVALID_AUTH_CODE);

        return ResponseEntity.ok()
                    .header(ACCESS_TOKEN.getValue(), token)
                    .body(SuccessResponse.noContent());
    }

    @Operation(summary = "ID/PW 찾기 전화번호 인증")
    @Parameters({
            @Parameter(name = "type", description = "인증 타입(uid, password)", in = ParameterIn.QUERY, required = true),
            @Parameter(name = "dto", description = "전화번호", schema = @Schema(implementation = SmsReq.class))
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "인증번호 전송 성공", content = @Content(schema = @Schema(implementation = SuccessResponse.class))),
            @ApiResponse(responseCode = "200", description = "인증번호 전송 실패", content = @Content(schema = @Schema(implementation = FailureResponse.class))),
            @ApiResponse(responseCode = "4xx", description = "에러", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
    })
    @PostMapping("/search-sms")
    @PreAuthorize("isAnonymous()")
    public ResponseEntity<?> searchSmsAuthorization(
        @RequestParam(value = "type") SmsPrefix type,
        @RequestParam(value = "code", required = false) String code,
        @RequestBody @Valid SmsReq dto
    ) {
        if (code == null) {
            SmsRes smsRes = memberAuthService.sendCertificationNumber(dto, type);
            return ResponseEntity.ok(SuccessResponse.from(smsRes));
        }
        memberAuthService.checkCertificationForSearch(dto, code, type);
        return ResponseEntity.ok(SuccessResponse.noContent());
    }

    @Operation(summary = "로그인", description = "유저 닉네임, 패스워드를 입력받고 유효하다면 액세스 토큰(헤더)과 리프레시 토큰(쿠키)을 반환합니다.")
    @Parameters({
            @Parameter(name = "dto", description = "로그인 정보", schema = @Schema(implementation = SignInReq.class))
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "로그인 성공", content = @Content(schema = @Schema(implementation = SuccessResponse.class))),
            @ApiResponse(responseCode = "400", description = "로그인 실패", content = @Content(schema = @Schema(implementation = FailureResponse.class))),
            @ApiResponse(responseCode = "4xx", description = "에러", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
    })
    @PostMapping("/login")
    @PreAuthorize("isAnonymous()")
    public ResponseEntity<?> signIn(@RequestHeader(value = "Authorization", required = false) String accessToken, @RequestBody @Valid SignInReq dto) {
        if (accessToken != null)
            throw new GlobalErrorException(ErrorCode.ALREADY_LOGIN_USER);
        Map<String, String> tokens = memberAuthService.login(dto);
        return getResponseEntity(tokens);
    }

    @Operation(summary = "로그아웃", description = "액세스 토큰과 리프레시 토큰을 만료시킵니다.")
    @Parameters({
            @Parameter(name = "Authorization", description = "액세스 토큰", in = ParameterIn.HEADER),
            @Parameter(name = "refreshToken", description = "리프레시 토큰", in = ParameterIn.COOKIE)
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "로그아웃 성공", content = @Content(schema = @Schema(implementation = SuccessResponse.class))),
            @ApiResponse(responseCode = "400", description = "로그아웃 실패", content = @Content(schema = @Schema(implementation = FailureResponse.class))),
            @ApiResponse(responseCode = "4xx", description = "에러", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
    })
    @GetMapping("/logout")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> signOut(
            @AccessTokenInfo AccessToken accessToken,
            @CookieValue(value = "refreshToken", required = false) @Valid String refreshToken,
            HttpServletRequest request, HttpServletResponse response) {
        if (accessToken.isReissued()) {
            refreshToken = response.getHeader(HttpHeaders.SET_COOKIE).substring(AuthConstants.REFRESH_TOKEN.getValue().length() + 1);
            log.info("reissued refresh token: {}", refreshToken);
        }

        memberAuthService.logout(accessToken, refreshToken);

        if (!StringUtils.hasText(refreshToken)) {
            return ResponseEntity.ok(SuccessResponse.noContent());
        }

        ResponseCookie cookie = cookieUtil.deleteCookie(request, response, REFRESH_TOKEN.getValue())
                .orElseThrow(() -> new AuthErrorException(AuthErrorCode.REFRESH_TOKEN_NOT_FOUND, "존재하지 않는 쿠키입니다."));
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString()).body(SuccessResponse.noContent());
    }

    @Operation(summary = "토큰 갱신", description = "리프레시 토큰을 이용해 액세스 토큰과 리프레시 토큰을 갱신합니다.")
    @Parameter(name = "refreshToken", description = "리프레시 토큰", in = ParameterIn.COOKIE)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "토큰 갱신 성공", content = @Content(schema = @Schema(implementation = SuccessResponse.class))),
            @ApiResponse(responseCode = "400", description = "토큰 갱신 실패", content = @Content(schema = @Schema(implementation = FailureResponse.class))),
            @ApiResponse(responseCode = "4xx", description = "에러", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
    })
    @GetMapping("/refresh")
    public ResponseEntity<?> refresh(@CookieValue("refreshToken") @Valid String refreshToken) {
        Map<String, String> tokens = memberAuthService.refresh(refreshToken);
        return getResponseEntity(tokens);
    }

    /**
     * 액세스 토큰과 리프레시 토큰을 반환합니다.
     * @param tokens : 액세스 토큰과 리프레시 토큰
     * @return ResponseEntity<?>
     */
    private ResponseEntity<?> getResponseEntity(Map<String, String> tokens) {
        ResponseCookie cookie = cookieUtil.createCookie(REFRESH_TOKEN.getValue(), tokens.get(REFRESH_TOKEN.getValue()), 60 * 60 * 24 * 7);

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .header(ACCESS_TOKEN.getValue(), tokens.get(ACCESS_TOKEN.getValue()))
                .body(SuccessResponse.noContent());
    }
}
