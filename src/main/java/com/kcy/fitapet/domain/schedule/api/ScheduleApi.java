package com.kcy.fitapet.domain.schedule.api;

import com.kcy.fitapet.domain.schedule.dto.ScheduleSaveDto;
import com.kcy.fitapet.domain.schedule.service.component.ScheduleManageService;
import com.kcy.fitapet.global.common.response.SuccessResponse;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v2/users/{user_id}/pets/{pet_id}/schedules")
public class ScheduleApi {
    private final ScheduleManageService scheduleManageService;

    @Operation(summary = "스케줄 등록")
    @PostMapping("")
    @PreAuthorize("isAuthenticated() and #userId == principal.id and managerAuthorize.isManager(#userId, #petId)")
    public ResponseEntity<?> saveSchedule(
            @PathVariable("user_id") Long userId,
            @PathVariable("pet_id") Long petId,
            @RequestBody @Valid ScheduleSaveDto.Request request
    ) {
        scheduleManageService.saveSchedule(petId, request);
        return ResponseEntity.ok(SuccessResponse.noContent());
    }
}
