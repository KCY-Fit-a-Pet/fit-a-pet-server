package com.kcy.fitapet.domain.pet.api;

import com.kcy.fitapet.domain.pet.dto.PetRegisterReq;
import com.kcy.fitapet.domain.pet.service.component.PetManageService;
import com.kcy.fitapet.global.common.response.ErrorResponse;
import com.kcy.fitapet.global.common.response.FailureResponse;
import com.kcy.fitapet.global.common.response.SuccessResponse;
import com.kcy.fitapet.global.common.security.authentication.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "반려동물 관리 API")
@RestController
@RequestMapping("/api/v2/pets")
@RequiredArgsConstructor
@Slf4j
public class PetManageApi {
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
    public ResponseEntity<?> savePet(@RequestBody @Valid PetRegisterReq req, @AuthenticationPrincipal CustomUserDetails user) {
        petManageService.savePet(req.toPetEntity(), user.getUserId());

        return ResponseEntity.ok(SuccessResponse.noContent());
    }


}
