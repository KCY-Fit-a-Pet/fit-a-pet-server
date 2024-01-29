package com.kcy.fitapet.domain.schedule.api;

import com.kcy.fitapet.domain.schedule.dto.ScheduleSaveDto;
import com.kcy.fitapet.domain.schedule.service.component.ScheduleManageService;
import com.kcy.fitapet.global.common.response.SuccessResponse;
import com.kcy.fitapet.global.common.security.authentication.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
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

@Tag(name = "스케줄 API", description = "반려동물 일정 관리 API")
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v2")
public class ScheduleApi {
    private final ScheduleManageService scheduleManageService;

    @Operation(summary = "스케줄 등록")
    @PostMapping("/schedules")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> saveSchedule(@RequestBody @Valid ScheduleSaveDto.Request request, @AuthenticationPrincipal CustomUserDetails principal) {
        scheduleManageService.saveSchedule(principal.getUserId(), request);
        return ResponseEntity.ok(SuccessResponse.noContent());
    }

    @Operation(summary = "pet_id에 속하는 반려동물 스케줄 조회", description = "count가 null이면 전체 조회, null이 아니면 현재 날짜&시간 이후 count 만큼 조회")
    @GetMapping("/pets/{pet_id}/schedules")
    @PreAuthorize("isAuthenticated() and @managerAuthorize.isManager(principal.userId, #petId)")
    public ResponseEntity<?> getPetSchedules(
            @PathVariable("pet_id") Long petId,
            @RequestParam(value = "count", required = false, defaultValue = "-1") int count
    ) {
        return ResponseEntity.ok(SuccessResponse.from("schedules", scheduleManageService.findPetSchedules(petId, count).getSchedules()));
    }
}
