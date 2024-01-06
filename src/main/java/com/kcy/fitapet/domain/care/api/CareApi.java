package com.kcy.fitapet.domain.care.api;

import com.kcy.fitapet.domain.care.dto.CareSaveDto;
import com.kcy.fitapet.global.common.response.SuccessResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Care", description = "케어 API")
@Slf4j
@RestController
@RequestMapping("/api/v1/pets/{pet_id}/cares")
@RequiredArgsConstructor
public class CareApi {

    @Operation(summary = "케어 등록")
    @Parameter(name = "pet_id", description = "등록할 반려동물 ID", required = true)
    @PostMapping("")
    public ResponseEntity<?> saveCare(
            @PathVariable("pet_id") Long petId,
            @RequestBody CareSaveDto.Request request
    ) {
        log.info("request: {}", request);

        return ResponseEntity.ok(SuccessResponse.noContent());
    }
}
