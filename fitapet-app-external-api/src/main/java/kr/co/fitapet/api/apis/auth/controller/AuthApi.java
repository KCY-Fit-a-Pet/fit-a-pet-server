package kr.co.fitapet.api.apis.auth.controller;

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
import kr.co.fitapet.api.apis.auth.dto.SmsReq;
import kr.co.fitapet.api.apis.auth.dto.SmsRes;
import kr.co.fitapet.api.apis.auth.usecase.MemberAuthUseCase;
import kr.co.fitapet.api.common.resolver.access.AccessTokenInfo;
import kr.co.fitapet.api.common.response.ErrorResponse;
import kr.co.fitapet.api.common.response.FailureResponse;
import kr.co.fitapet.api.common.response.SuccessResponse;
import kr.co.fitapet.api.common.security.jwt.dto.Jwt;
import kr.co.fitapet.api.common.security.jwt.exception.AuthErrorCode;
import kr.co.fitapet.api.common.security.jwt.exception.AuthErrorException;
import kr.co.fitapet.api.common.util.cookie.CookieUtil;
import kr.co.fitapet.common.execption.GlobalErrorException;
import kr.co.fitapet.domain.common.redis.sms.type.SmsPrefix;
import kr.co.fitapet.domain.domains.member.domain.AccessToken;
import kr.co.fitapet.domain.domains.member.dto.auth.SignInReq;
import kr.co.fitapet.domain.domains.member.dto.auth.SignUpReq;
import kr.co.fitapet.infra.client.sms.snes.exception.SmsErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

import static kr.co.fitapet.api.common.security.jwt.AuthConstants.ACCESS_TOKEN;
import static kr.co.fitapet.api.common.security.jwt.AuthConstants.REFRESH_TOKEN;


@Tag(name = "유저 관리 API", description = "유저 인증과 관련된 API")
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthApi {
    private final MemberAuthUseCase memberAuthUseCase;
    private final CookieUtil cookieUtil;

    @Operation(summary = "회원가입", description = "유저 닉네임, 패스워드를 입력받고 유효하다면 액세스 토큰(헤더)과 리프레시 토큰(쿠키)을 반환합니다.")
    @Parameter(name = "Authorization", description = "액세스 토큰", in = ParameterIn.HEADER)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "회원가입 성공", content = @Content(schema = @Schema(implementation = SuccessResponse.class))),
            @ApiResponse(responseCode = "400", description = "회원가입 실패", content = @Content(schema = @Schema(implementation = FailureResponse.class))),
            @ApiResponse(responseCode = "4xx", description = "에러", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
    })
    @PostMapping("/register")
    @PreAuthorize("isAnonymous()")
    public ResponseEntity<?> signUp(@RequestHeader("Authorization") @NotBlank String accessToken, @RequestBody @Valid SignUpReq dto) {
        Pair<Long, Jwt> result = memberAuthUseCase.register(accessToken, dto);
        return getResponseEntity(result.getKey(), result.getValue());
    }

    @Operation(summary = "회원가입 전화번호 인증")
    @Parameter(name = "code", description = "인증번호", in = ParameterIn.QUERY)
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
            SmsRes smsRes = memberAuthUseCase.sendCode(dto, SmsPrefix.REGISTER);
            return ResponseEntity.ok(SuccessResponse.from(smsRes));
        }

        Jwt token = memberAuthUseCase.checkCodeForRegister(dto, code);
        if (token == null)
            throw new GlobalErrorException(SmsErrorCode.INVALID_AUTH_CODE);
        else if (token.refreshToken() == null)
            return ResponseEntity.ok()
                    .header(ACCESS_TOKEN.getValue(), token.accessToken())
                    .body(SuccessResponse.noContent());

        ResponseCookie cookie = cookieUtil.createCookie(REFRESH_TOKEN.getValue(), token.refreshToken(), 60 * 60 * 24 * 7);

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .header(ACCESS_TOKEN.getValue(), token.accessToken())
                .body(SuccessResponse.from(Map.of("member", "등록된 oauth 계정 연동 성공")));
    }

    @Operation(summary = "ID/PW 찾기 전화번호 인증")
    @Parameters({
            @Parameter(name = "code", description = "인증번호", in = ParameterIn.QUERY),
            @Parameter(name = "type", description = "인증 타입(uid, password)", in = ParameterIn.QUERY, required = true)
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
            SmsRes smsRes = memberAuthUseCase.sendCode(dto, type);
            return ResponseEntity.ok(SuccessResponse.from(smsRes));
        }
        memberAuthUseCase.checkCodeForSearch(dto, code, type);
        return ResponseEntity.ok(SuccessResponse.noContent());
    }

    @Operation(summary = "로그인", description = "유저 닉네임, 패스워드를 입력받고 유효하다면 액세스 토큰(헤더)과 리프레시 토큰(쿠키)을 반환합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "로그인 성공", content = @Content(schema = @Schema(implementation = SuccessResponse.class))),
            @ApiResponse(responseCode = "400", description = "로그인 실패", content = @Content(schema = @Schema(implementation = FailureResponse.class))),
            @ApiResponse(responseCode = "4xx", description = "에러", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
    })
    @PostMapping("/login")
    @PreAuthorize("isAnonymous()")
    public ResponseEntity<?> signIn(@RequestBody @Valid SignInReq dto) {
        Pair<Long, Jwt> result = memberAuthUseCase.login(dto);

        return getResponseEntity(result.getKey(), result.getValue());
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
            @CookieValue(value = "refreshToken") @Valid String refreshToken,
            HttpServletRequest request, HttpServletResponse response) {
        memberAuthUseCase.logout(accessToken, refreshToken);
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
    @PreAuthorize("isAnonymous()")
    public ResponseEntity<?> refresh(@CookieValue("refreshToken") @Valid String refreshToken) {
        Jwt tokens = memberAuthUseCase.refresh(refreshToken);

        ResponseCookie cookie = cookieUtil.createCookie(REFRESH_TOKEN.getValue(), tokens.refreshToken(), 60 * 60 * 24 * 7);

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .header(ACCESS_TOKEN.getValue(), tokens.accessToken())
                .body(SuccessResponse.noContent());
    }

    @Operation(summary = "토큰 검증", description = "액세스 토큰의 유효성을 검사합니다.")
    @Parameter(name = "Authorization", description = "액세스 토큰", in = ParameterIn.HEADER)
    @GetMapping("/verify")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> verify(@AccessTokenInfo AccessToken accessToken) {
        return ResponseEntity.ok(SuccessResponse.from("userId", accessToken.userId()));
    }

    /**
     * 액세스 토큰과 리프레시 토큰을 반환합니다.
     * @param tokens : 액세스 토큰과 리프레시 토큰
     * @return ResponseEntity<?>
     */
    private ResponseEntity<?> getResponseEntity(Long userId, Jwt tokens) {
        ResponseCookie cookie = cookieUtil.createCookie(REFRESH_TOKEN.getValue(), tokens.refreshToken(), 60 * 60 * 24 * 7);

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .header(ACCESS_TOKEN.getValue(), tokens.accessToken())
                .body(SuccessResponse.from(Map.of("userId", userId)));
    }
}
