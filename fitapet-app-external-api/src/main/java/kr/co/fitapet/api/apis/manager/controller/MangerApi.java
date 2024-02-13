package kr.co.fitapet.api.apis.manager.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.co.fitapet.api.apis.manager.usecase.ManagerUseCase;
import kr.co.fitapet.api.common.response.SuccessResponse;
import kr.co.fitapet.api.common.security.authentication.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "매니저 API", description = "반려동물 관리자 및 어드민의 기능을 제공하는 API")
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v2/pets/{pet_id}/managers")
public class MangerApi {
    private final ManagerUseCase managerUseCase;

    @Operation(summary = "매니저 목록 조회")
    @Parameter(name = "pet_id", description = "반려동물 ID", in = ParameterIn.PATH, required = true)
    @GetMapping("")
    @PreAuthorize("isAuthenticated() and @managerAuthorize.isManager(principal.userId, #petId)")
    public ResponseEntity<?> getManagers(@PathVariable("pet_id") Long petId, @AuthenticationPrincipal CustomUserDetails userDetails) {
        return ResponseEntity.ok(SuccessResponse.from("managers", managerUseCase.findManagers(petId, userDetails.getUserId())));
    }



    @Operation(summary = "매니저 추방")
    @DeleteMapping("/{manager_id}")
    @PreAuthorize("isAuthenticated() and @managerAuthorize.isMaster(principal.userId, #petId) and @managerAuthorize.isManager(#managerId, #petId)")
    public ResponseEntity<?> deleteManager(@PathVariable("pet_id") Long petId, @PathVariable("manager_id") Long managerId, @AuthenticationPrincipal CustomUserDetails userDetails) {
        return null;
    }
}
