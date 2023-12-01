package com.kcy.fitapet.domain.member.api;

import com.kcy.fitapet.domain.member.dto.account.AccountProfileRes;
import com.kcy.fitapet.domain.member.dto.account.AccountSearchReq;
import com.kcy.fitapet.domain.member.dto.account.ProfilePatchReq;
import com.kcy.fitapet.domain.member.dto.account.UidRes;
import com.kcy.fitapet.domain.member.exception.AccountErrorCode;
import com.kcy.fitapet.domain.member.service.component.MemberAccountService;
import com.kcy.fitapet.domain.member.type.MemberAttrType;
import com.kcy.fitapet.domain.notification.type.NotificationType;
import com.kcy.fitapet.global.common.response.SuccessResponse;
import com.kcy.fitapet.global.common.response.exception.GlobalErrorException;
import com.kcy.fitapet.global.common.security.authentication.CustomUserDetails;
import com.kcy.fitapet.global.common.redis.sms.SmsPrefix;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "프로필 API")
@RestController
@Slf4j
@RequestMapping("/api/v1/accounts")
@RequiredArgsConstructor
public class AccountApi {
    private final MemberAccountService memberAccountService;

    @Operation(summary = "프로필 조회")
    @GetMapping("")
    public ResponseEntity<?> getProfile(@AuthenticationPrincipal CustomUserDetails user) {
        AccountProfileRes member = memberAccountService.getProfile(user.getUserId());
        return ResponseEntity.ok(SuccessResponse.from(member));
    }

    @Operation(summary = "프로필(비밀번호/이름) 수정")
    @Parameters({
            @Parameter(name = "type", description = "수정할 프로필 타입", example = "name/password/", required = true),
            @Parameter(name = "req", description = "수정할 프로필 정보")
    })
    @PutMapping("")
    public ResponseEntity<?> putProfile(
            @AuthenticationPrincipal CustomUserDetails user,
            @RequestParam("type") @NotBlank MemberAttrType type,
            @RequestBody ProfilePatchReq req
    ) {
        log.info("type: {}", type);
        memberAccountService.updateProfile(user.getUserId(), req, type);

        return ResponseEntity.ok(SuccessResponse.noContent());
    }

    @Operation(summary = "ID/PW 찾기")
    @PostMapping("/search")
    @Parameters({
            @Parameter(name = "type", description = "찾을 타입", example = "uid/password", required = true),
            @Parameter(name = "code", description = "인증번호", required = true),
            @Parameter(name = "req", description = "uid(이름)/password(비밀번호)")
    })
    public ResponseEntity<?> postSearchIdOrPassword(
            @RequestParam("type") @NotBlank SmsPrefix type,
            @RequestParam("code") @NotBlank String code,
            @RequestBody AccountSearchReq req
    ) {
        if (type.equals(SmsPrefix.UID)) {
            UidRes res = memberAccountService.getUidWhenSmsAuthenticated(req.phone(), code, type);
            return ResponseEntity.ok(SuccessResponse.from(res));
        } else {
            memberAccountService.overwritePassword(req, code, type);
            return ResponseEntity.ok(SuccessResponse.noContent());
        }
    }

    @Operation(summary = "알림 on/off")
    @Parameter(name = "type", description = "알림 타입", example = "care or memo or schedule", required = true)
    @GetMapping("/notify")
    public ResponseEntity<?> putNotify(@AuthenticationPrincipal CustomUserDetails user, @RequestParam("type") @NotBlank NotificationType type) {
        memberAccountService.updateNotification(user.getUserId(), type);
        return ResponseEntity.ok(SuccessResponse.noContent());
    }


}
