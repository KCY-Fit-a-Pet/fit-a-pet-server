package com.kcy.fitapet.domain.member.api;

import com.kcy.fitapet.domain.member.dto.profile.MemberProfileRes;
import com.kcy.fitapet.domain.member.dto.profile.ProfilePatchReq;
import com.kcy.fitapet.domain.member.service.component.MemberProfileService;
import com.kcy.fitapet.global.common.response.SuccessResponse;
import com.kcy.fitapet.global.common.security.authentication.CustomUserDetails;
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
@RequestMapping("/api/v1/u/accounts")
@RequiredArgsConstructor
public class ProfileApi {
    private final MemberProfileService memberProfileService;

    @Operation(summary = "프로필 조회")
    @GetMapping("")
    public ResponseEntity<?> getProfile(@AuthenticationPrincipal CustomUserDetails user) {
        MemberProfileRes member = memberProfileService.getProfile(user.getUserId());
        return ResponseEntity.ok(SuccessResponse.from(member));
    }

    @Operation(summary = "프로필(비밀번호/이름) 수정")
    @Parameters({
            @Parameter(name = "type", description = "수정할 프로필 타입", example = "name or password", required = true),
            @Parameter(name = "req", description = "수정할 프로필 정보")
    })
    @PutMapping("")
    public ResponseEntity<?> putProfile(
            @AuthenticationPrincipal CustomUserDetails user,
            @RequestParam("type") @NotBlank String type,
            @RequestBody ProfilePatchReq req
    ) {
        if (type.equals("name")) {
            memberProfileService.updateName(user.getUserId(), req);
        } else if (type.equals("password")) {
            memberProfileService.updatePassword(user.getUserId(), req);
        }

        return ResponseEntity.ok(SuccessResponse.noContent());
    }

    @Operation(summary = "알림 on/off")
    @Parameter(name = "type", description = "알림 타입", example = "care or memo or schedule", required = true)
    @GetMapping("/notify")
    public ResponseEntity<?> putNotify(@AuthenticationPrincipal CustomUserDetails user, @RequestParam("type") @NotBlank String type) {
        memberProfileService.updateNotification(user.getUserId(), type);
        return ResponseEntity.ok(SuccessResponse.noContent());
    }
}
