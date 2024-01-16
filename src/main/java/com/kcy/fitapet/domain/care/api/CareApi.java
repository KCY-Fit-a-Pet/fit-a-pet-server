package com.kcy.fitapet.domain.care.api;

import com.kcy.fitapet.domain.care.dto.CareInfoRes;
import com.kcy.fitapet.domain.care.dto.CareSaveReq;
import com.kcy.fitapet.domain.care.service.component.CareManageService;
import com.kcy.fitapet.domain.log.dto.CareLogInfo;
import com.kcy.fitapet.global.common.response.SuccessResponse;
import com.kcy.fitapet.global.common.security.authentication.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Tag(name = "Care", description = "케어 API")
@Slf4j
@RestController
@RequestMapping("/api/v2/users/{user_id}/pets/{pet_id}/cares")
@RequiredArgsConstructor
public class CareApi {
    private final CareManageService careManageService;

    @Operation(summary = "케어 등록")
    @Parameters({
            @Parameter(name = "user_id", description = "등록할 유저 ID", required = true),
            @Parameter(name = "pet_id", description = "등록할 반려동물 ID", required = true)
    })
    @PostMapping("")
    @PreAuthorize("isAuthenticated() and #userId == principal.userId and @managerAuthorize.isManager(principal.userId, #petId)")
    public ResponseEntity<?> saveCare(
            @PathVariable("user_id") Long userId,
            @PathVariable("pet_id") Long petId,
            @RequestBody @Valid CareSaveReq.Request request,
            @AuthenticationPrincipal CustomUserDetails user
            ) {
        careManageService.saveCare(user.getUserId(), petId, request);

        return ResponseEntity.ok(SuccessResponse.noContent());
    }

    @Operation(summary = "케어 목록 조회")
    @Parameters({
            @Parameter(name = "user_id", description = "등록할 유저 ID", required = true),
            @Parameter(name = "pet_id", description = "등록할 반려동물 ID", required = true)
    })
    @GetMapping("")
    @PreAuthorize("isAuthenticated() and #userId == principal.userId and @managerAuthorize.isManager(principal.userId, #petId)")
    public ResponseEntity<?> getCares(
            @PathVariable("user_id") Long userId,
            @PathVariable("pet_id") Long petId
    ) {
        CareInfoRes res = careManageService.findCaresByPetId(petId);
        return ResponseEntity.ok(SuccessResponse.from("careCategories", res.getInfo()));
    }

    @Operation(summary = "작성한 케어 카테고리 목록 조회")
    @GetMapping("/categories")
    @PreAuthorize("isAuthenticated() and #userId == principal.userId and @managerAuthorize.isManager(principal.userId, #petId)")
    public ResponseEntity<?> getCareCategoryNames(
            @PathVariable("user_id") Long userId,
            @PathVariable("pet_id") Long petId
    ) {
        List<?> careCategories = careManageService.findCareCategoryNamesByPetId(petId);
        return ResponseEntity.ok(SuccessResponse.from("careCategories", careCategories));
    }

    @Operation(summary = "케어 수행")
    @GetMapping("/{care_id}/care-dates/{care_date_id}")
    @PreAuthorize("isAuthenticated() and #userId == principal.userId and @managerAuthorize.isManager(principal.userId, #petId)") // TODO: careDate가 care에 속하는 지 확인
    public ResponseEntity<?> doCare(
            @PathVariable("user_id") Long userId,
            @PathVariable("pet_id") Long petId,
            @PathVariable("care_id") Long careId,
            @PathVariable("care_date_id") Long careDateId
    ) {
        CareLogInfo careLog = careManageService.doCare(careDateId, userId);
        return ResponseEntity.ok(SuccessResponse.from(careLog));
    }
}
