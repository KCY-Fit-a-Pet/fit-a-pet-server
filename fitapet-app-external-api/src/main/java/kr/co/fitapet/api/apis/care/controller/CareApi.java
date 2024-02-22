package kr.co.fitapet.api.apis.care.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import kr.co.fitapet.api.apis.care.usecase.CareUseCase;
import kr.co.fitapet.api.common.response.SuccessResponse;
import kr.co.fitapet.api.common.security.authentication.CustomUserDetails;
import kr.co.fitapet.domain.domains.care.dto.CareInfoRes;
import kr.co.fitapet.domain.domains.care.dto.CareSaveReq;
import kr.co.fitapet.domain.domains.care_log.dto.CareLogInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Care", description = "케어 API")
@Slf4j
@RestController
@RequestMapping("/api/v2/pets/{pet_id}/cares")
@RequiredArgsConstructor
public class CareApi {
    private final CareUseCase careUseCase;

    @Operation(summary = "케어 등록")
    @Parameter(name = "pet_id", description = "등록할 반려동물 ID", in = ParameterIn.PATH, required = true)
    @PostMapping("")
    @PreAuthorize("isAuthenticated() and @managerAuthorize.isManager(principal.userId, #petId)")
    public ResponseEntity<?> saveCare(
            @PathVariable("pet_id") Long petId,
            @RequestBody @Valid CareSaveReq.Request request,
            @AuthenticationPrincipal CustomUserDetails user
    ) {
        careUseCase.saveCare(user.getUserId(), petId, request);
        return ResponseEntity.ok(SuccessResponse.noContent());
    }

    @Operation(summary = "케어 목록 조회")
    @Parameter(name = "pet_id", description = "등록할 반려동물 ID", in = ParameterIn.PATH, required = true)
    @GetMapping("")
    @PreAuthorize("isAuthenticated() and @managerAuthorize.isManager(principal.userId, #petId)")
    public ResponseEntity<?> getCares(@PathVariable("pet_id") Long petId) {
        CareInfoRes res = careUseCase.findCaresByPetId(petId);
        return ResponseEntity.ok(SuccessResponse.from("careCategories", res.getInfo()));
    }

    @Operation(summary = "작성한 케어 카테고리 목록 조회")
    @Parameter(name = "pet_id", description = "등록할 반려동물 ID", in = ParameterIn.PATH, required = true)
    @GetMapping("/categories")
    @PreAuthorize("isAuthenticated() and @managerAuthorize.isManager(principal.userId, #petId)")
    public ResponseEntity<?> getCareCategoryNames(@PathVariable("pet_id") Long petId) {
        List<?> careCategories = careUseCase.findCareCategoryNamesByPetId(petId);
        return ResponseEntity.ok(SuccessResponse.from("careCategories", careCategories));
    }

    @Operation(summary = "케어 수정")
    @Parameters({
            @Parameter(name = "pet_id", description = "등록할 반려동물 ID", in = ParameterIn.PATH, required = true),
            @Parameter(name = "care_id", description = "케어 ID", in = ParameterIn.PATH, required = true)
    })
    @PutMapping("/{care_id}")
    @PreAuthorize("isAuthenticated() and @managerAuthorize.isManager(principal.userId, #petId)")
    public ResponseEntity<?> updateCare(
            @PathVariable("pet_id") Long petId,
            @PathVariable("care_id") Long careId,
            @RequestBody @Valid CareSaveReq.Request request,
            @AuthenticationPrincipal CustomUserDetails user
    ) {
//        careUseCase.updateCare(careId, user.getUserId(), petId, request);
        return ResponseEntity.ok(SuccessResponse.noContent());
    }

    @Operation(summary = "케어 수행")
    @Parameters({
            @Parameter(name = "pet_id", description = "등록할 반려동물 ID", in = ParameterIn.PATH, required = true),
            @Parameter(name = "care_id", description = "케어 ID", in = ParameterIn.PATH, required = true),
            @Parameter(name = "care_date_id", description = "케어 날짜 ID", in = ParameterIn.PATH, required = true)
    })
    @GetMapping("/{care_id}/care-dates/{care_date_id}")
    @PreAuthorize("isAuthenticated() and @managerAuthorize.isManager(principal.userId, #petId) and @careAuthorize.isValidCareAndCareDate(#petId, #careId, #careDateId)")
    public ResponseEntity<?> doCare(
            @PathVariable("pet_id") Long petId,
            @PathVariable("care_id") Long careId,
            @PathVariable("care_date_id") Long careDateId,
            @AuthenticationPrincipal CustomUserDetails user
    ) {
        CareLogInfo careLog = careUseCase.doCare(careDateId, user.getUserId());
        return ResponseEntity.ok(SuccessResponse.from(careLog));
    }

    @Operation(summary = "케어 수행 취소")
    @Parameters({
            @Parameter(name = "pet_id", description = "등록할 반려동물 ID", in = ParameterIn.PATH, required = true),
            @Parameter(name = "care_id", description = "케어 ID", in = ParameterIn.PATH, required = true),
            @Parameter(name = "care_date_id", description = "케어 날짜 ID", in = ParameterIn.PATH, required = true)
    })
    @DeleteMapping("/{care_id}/care-dates/{care_date_id}")
    @PreAuthorize("isAuthenticated() and @managerAuthorize.isManager(principal.userId, #petId) and @careAuthorize.isValidCareAndCareDate(#petId, #careId, #careDateId)")
    public ResponseEntity<?> cancleCare(
            @PathVariable("pet_id") Long petId,
            @PathVariable("care_id") Long careId,
            @PathVariable("care_date_id") Long careDateId
    ) {
        careUseCase.cancelCare(careDateId);
        return ResponseEntity.ok(SuccessResponse.noContent());
    }

    @Operation(summary = "케어 삭제")
    @Parameters({
            @Parameter(name = "pet_id", description = "등록할 반려동물 ID", in = ParameterIn.PATH, required = true),
            @Parameter(name = "care_id", description = "케어 ID", in = ParameterIn.PATH, required = true)
    })
    @DeleteMapping("/{care_id}")
    @PreAuthorize("isAuthenticated() and @managerAuthorize.isManager(principal.userId, #petId)")
    public ResponseEntity<?> deleteCare(
            @PathVariable("pet_id") Long petId,
            @PathVariable("care_id") Long careId,
            @AuthenticationPrincipal CustomUserDetails user
    ) {
//        careUseCase.deleteCare(careId, user.getUserId());
        return ResponseEntity.ok(SuccessResponse.noContent());
    }
}
