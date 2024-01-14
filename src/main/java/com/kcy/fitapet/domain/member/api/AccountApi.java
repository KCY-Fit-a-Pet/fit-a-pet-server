package com.kcy.fitapet.domain.member.api;

import com.kcy.fitapet.domain.member.dto.account.AccountProfileRes;
import com.kcy.fitapet.domain.member.dto.account.AccountSearchReq;
import com.kcy.fitapet.domain.member.dto.account.ProfilePatchReq;
import com.kcy.fitapet.domain.member.dto.account.UidRes;
import com.kcy.fitapet.domain.member.service.component.MemberAccountService;
import com.kcy.fitapet.domain.member.type.MemberAttrType;
import com.kcy.fitapet.domain.notification.type.NotificationType;
import com.kcy.fitapet.domain.pet.dto.PetInfoRes;
import com.kcy.fitapet.global.common.response.SuccessResponse;
import com.kcy.fitapet.global.common.security.authentication.CustomUserDetails;
import com.kcy.fitapet.global.common.redis.sms.type.SmsPrefix;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Tag(name = "프로필 API")
@RestController
@Slf4j
@RequestMapping("/api/v2/accounts")
@RequiredArgsConstructor
public class AccountApi {
    private final MemberAccountService memberAccountService;

    @Operation(summary = "프로필 조회")
    @Parameter(name = "id", description = "조회할 프로필 ID", in = ParameterIn.PATH, required = true)
    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated() and #id == principal.userId")
    public ResponseEntity<?> getProfile(@PathVariable("id") Long id) {
        AccountProfileRes member = memberAccountService.getProfile(id);
        return ResponseEntity.ok(SuccessResponse.from(member));
    }

    @Operation(summary = "닉네임 존재 확인")
    @GetMapping("/exists")
    @Parameter(name = "uid", description = "확인할 유저 닉네임", in = ParameterIn.QUERY, required = true)
    @PreAuthorize("isAnonymous()")
    public ResponseEntity<?> getExistsUid(@RequestParam("uid") @NotBlank String uid) {
        boolean exists = memberAccountService.existsUid(uid);
        return ResponseEntity.ok(SuccessResponse.from(Map.of("valid", exists)));
    }

    @Operation(summary = "프로필(비밀번호/이름) 수정")
    @Parameters({
            @Parameter(name = "id", description = "수정할 프로필 ID", in = ParameterIn.PATH, required = true),
            @Parameter(name = "type", description = "수정할 프로필 타입", in = ParameterIn.QUERY, required = true),
    })
    @PutMapping("/{id}")
    @PreAuthorize("isAuthenticated() and #id == principal.userId")
    public ResponseEntity<?> putProfile(
            @PathVariable Long id,
            @RequestParam("type") MemberAttrType type,
            @RequestBody @Valid ProfilePatchReq req
    ) {
        log.info("type: {}", type);
        memberAccountService.updateProfile(id, req, type);

        return ResponseEntity.ok(SuccessResponse.noContent());
    }

    @Operation(summary = "ID/PW 찾기")
    @PostMapping("/search")
    @Parameters({
            @Parameter(name = "type", description = "찾을 타입", example = "uid/password", in = ParameterIn.QUERY, required = true),
            @Parameter(name = "code", description = "인증번호", in = ParameterIn.QUERY, required = true),
    })
    @PreAuthorize("isAnonymous()")
    public ResponseEntity<?> postSearchIdOrPassword(
            @RequestParam("type") @NotBlank SmsPrefix type,
            @RequestParam("code") @NotBlank String code,
            @RequestBody @Valid AccountSearchReq req
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
    @Parameters({
            @Parameter(name = "id", description = "알림 설정할 ID", in = ParameterIn.PATH, required = true),
            @Parameter(name = "type", description = "알림 타입", example = "care or memo or schedule", required = true)
    })
    @GetMapping("/{id}/notify")
    @PreAuthorize("isAuthenticated() and #id == principal.userId")
    public ResponseEntity<?> putNotify(
            @PathVariable Long id,
            @AuthenticationPrincipal CustomUserDetails user,
            @RequestParam("type") @NotBlank NotificationType type) {
        memberAccountService.updateNotification(id, user.getUserId(), type);
        return ResponseEntity.ok(SuccessResponse.noContent());
    }

    @Operation(summary = "관리 중인 반려동물 pk 리스트 조회")
    @GetMapping("/{id}/pets")
    @PreAuthorize("isAuthenticated() and #id == principal.userId")
    public ResponseEntity<?> getPetIds(@PathVariable Long id) {
        List<Long> petIds = memberAccountService.getPetIds(id);
        return ResponseEntity.ok(SuccessResponse.from("pets", petIds));
    }
}
