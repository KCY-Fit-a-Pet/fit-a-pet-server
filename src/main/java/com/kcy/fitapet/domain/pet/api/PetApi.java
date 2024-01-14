package com.kcy.fitapet.domain.pet.api;

import com.kcy.fitapet.domain.care.dto.CareCategoryDto;
import com.kcy.fitapet.domain.pet.dto.PetSaveReq;
import com.kcy.fitapet.domain.pet.service.component.PetManageService;
import com.kcy.fitapet.global.common.response.ErrorResponse;
import com.kcy.fitapet.global.common.response.FailureResponse;
import com.kcy.fitapet.global.common.response.SuccessResponse;
import com.kcy.fitapet.global.common.security.authentication.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "반려동물 관리 API")
@RestController
@RequestMapping("/api/v2/users/{user_id}/pets")
@RequiredArgsConstructor
@Slf4j
public class PetApi {
    private final PetManageService petManageService;

    @Operation(summary = "반려동물 등록")
    @Parameter(name = "req", description = "반려동물 등록 요청", required = true)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "인증번호 전송 성공", content = @Content(schema = @Schema(implementation = SuccessResponse.class))),
            @ApiResponse(responseCode = "200", description = "인증번호 전송 실패", content = @Content(schema = @Schema(implementation = FailureResponse.class))),
            @ApiResponse(responseCode = "4xx", description = "에러", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
    })
    @PostMapping("")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> savePet(@RequestBody @Valid PetSaveReq req, @AuthenticationPrincipal CustomUserDetails user) {
        petManageService.savePet(req.toPetEntity(), user.getUserId());

        return ResponseEntity.ok(SuccessResponse.noContent());
    }

    @Operation(summary = "관리 중인 반려동물 목록 조회")
    @Parameter(name = "user_id", description = "조회할 유저 ID", required = true)
    @GetMapping("/summary")
    @PreAuthorize("isAuthenticated() and #userId == principal.userId")
    public ResponseEntity<?> findPets(@PathVariable("user_id") Long userId) {
        List<?> pets = petManageService.findPetsSummaryByUserId(userId).getPets();
        return ResponseEntity.ok(SuccessResponse.from("pets", pets));
    }

    @Operation(summary = "반려동물 케어 카테고리 유효성 검사")
    @Parameter(name = "user_id", description = "조회할 유저 ID", in = ParameterIn.PATH, required = true)
    @PostMapping("/categories-check")
    @PreAuthorize("isAuthenticated() and #userId == principal.userId")
    public ResponseEntity<?> checkCategoryExist(@PathVariable("user_id") Long userId, @RequestBody @Valid CareCategoryDto.CareCategoryExistRequest request) {
        List<?> result = petManageService.checkCategoryExist(userId, request.categoryName(), request.pets());
        return ResponseEntity.ok(SuccessResponse.from("categories", result));
    }
}
