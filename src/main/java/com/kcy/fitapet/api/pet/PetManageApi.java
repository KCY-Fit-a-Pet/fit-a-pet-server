package com.kcy.fitapet.api.pet;

import com.kcy.fitapet.domain.pet.dto.PetRegisterReq;
import com.kcy.fitapet.domain.pet.service.component.PetManageService;
import com.kcy.fitapet.global.common.response.SuccessResponse;
import com.kcy.fitapet.global.common.security.authentication.CustomUserDetails;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/pets")
@RequiredArgsConstructor
@Slf4j
public class PetManageApi {
    private final PetManageService petManageService;

    @PostMapping("")
    public ResponseEntity<?> savePet(@RequestBody @Valid PetRegisterReq req, @AuthenticationPrincipal CustomUserDetails user) {
        petManageService.savePet(req, user.getUserId());

        return ResponseEntity.ok(SuccessResponse.noContent());
    }
}
