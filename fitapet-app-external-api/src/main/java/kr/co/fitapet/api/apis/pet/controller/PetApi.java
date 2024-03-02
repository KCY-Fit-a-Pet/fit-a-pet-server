package kr.co.fitapet.api.apis.pet.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import kr.co.fitapet.api.apis.pet.usecase.PetUseCase;
import kr.co.fitapet.api.common.response.ErrorResponse;
import kr.co.fitapet.api.common.response.FailureResponse;
import kr.co.fitapet.api.common.response.SuccessResponse;
import kr.co.fitapet.api.common.security.authentication.CustomUserDetails;
import kr.co.fitapet.api.apis.care.dto.CareCategoryInfo;
import kr.co.fitapet.domain.domains.pet.dto.PetInfoRes;
import kr.co.fitapet.domain.domains.pet.dto.PetSaveReq;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "반려동물 관리 API")
@RestController
@RequestMapping("/api/v2/pets")
@RequiredArgsConstructor
@Slf4j
public class PetApi {
    private final PetUseCase petUseCase;

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
        petUseCase.savePet(req.toEntity(), user.getUserId());

        return ResponseEntity.ok(SuccessResponse.noContent());
    }

    @Operation(summary = "관리 중인 반려동물 리스트 조회")
    @GetMapping("")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> getPets(@AuthenticationPrincipal CustomUserDetails user) {
        PetInfoRes pets = petUseCase.findPets(user.getUserId());
        return ResponseEntity.ok(SuccessResponse.from("pets", pets.getPets()));
    }

    @Operation(summary = "관리 중인 반려동물 요약 목록 조회")
    @GetMapping("/summary")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> findPets(@AuthenticationPrincipal CustomUserDetails user) {
        List<?> pets = petUseCase.findPetsSummaryByUserId(user.getUserId()).getPets();
        return ResponseEntity.ok(SuccessResponse.from("pets", pets));
    }

    @Operation(summary = "반려동물 정보 조회")
    @GetMapping("/{pet_id}")
    @PreAuthorize("isAuthenticated() and @managerAuthorize.isManager(principal.userId, #petId)")
    public ResponseEntity<?> getPet(@PathVariable("pet_id") Long petId) {
        return ResponseEntity.ok(SuccessResponse.from("pet", petUseCase.findPet(petId)));
    }

    @Operation(summary = "반려동물 케어 카테고리 유효성 검사")
    @Parameter(name = "user_id", description = "조회할 유저 ID", in = ParameterIn.PATH, required = true)
    @PostMapping("/categories-check")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> checkCategoryExist(@AuthenticationPrincipal CustomUserDetails user, @RequestBody @Valid CareCategoryInfo.CareCategoryExistRequest request) {
        List<?> result = petUseCase.checkCategoryExist(user.getUserId(), request.categoryName(), request.pets());
        return ResponseEntity.ok(SuccessResponse.from("categories", result));
    }

    @Operation(summary = "반려동물 정보 수정")
    @Parameter(name = "pet_id", description = "수정할 반려동물 ID", in = ParameterIn.PATH, required = true)
    @PutMapping("/{pet_id}")
    @PreAuthorize("isAuthenticated() and @managerAuthorize.isMaster(principal.userId, #petId)")
    public ResponseEntity<?> updatePet(@PathVariable("pet_id") Long petId, @RequestBody @Valid PetSaveReq req) {
        petUseCase.updatePet(petId, req);
        return ResponseEntity.ok(SuccessResponse.noContent());
    }

    @Operation(summary = "반려동물 삭제")
    @Parameter(name = "pet_id", description = "삭제할 반려동물 ID", in = ParameterIn.PATH, required = true)
    @DeleteMapping("/{pet_id}")
    @PreAuthorize("isAuthenticated() and @managerAuthorize.isMaster(principal.userId, #petId)")
    public ResponseEntity<?> deletePet(@PathVariable("pet_id") Long petId, @AuthenticationPrincipal CustomUserDetails user) {
        petUseCase.deletePet(petId, user.getUserId());
        return ResponseEntity.ok(SuccessResponse.noContent());
    }

}
