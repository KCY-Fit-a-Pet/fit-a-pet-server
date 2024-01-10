package com.kcy.fitapet.domain.care.api;

import com.kcy.fitapet.domain.care.dto.CareSaveDto;
import com.kcy.fitapet.domain.care.service.component.CareManageService;
import com.kcy.fitapet.global.common.response.SuccessResponse;
import com.kcy.fitapet.global.common.security.authentication.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Care", description = "케어 API")
@Slf4j
@RestController
@RequestMapping("/api/v2/pets/{pet_id}/cares")
@RequiredArgsConstructor
public class CareApi {
    private final CareManageService careManageService;

    @Operation(summary = "케어 등록")
    @Parameter(name = "pet_id", description = "등록할 반려동물 ID", required = true)
    @PostMapping("")
    @PreAuthorize("isAuthenticated() && @careAuthorizeManager.isManager(#petId, principal.userId)")
    public ResponseEntity<?> saveCare(
            @PathVariable("pet_id") Long petId,
            @RequestBody CareSaveDto.Request request,
            @AuthenticationPrincipal CustomUserDetails user
            ) {
        log.info("request: {}", request);
        careManageService.saveCare(user.getUserId(), petId, request);

        return ResponseEntity.ok(SuccessResponse.noContent());
    }
}
