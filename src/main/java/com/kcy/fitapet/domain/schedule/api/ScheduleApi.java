package com.kcy.fitapet.domain.schedule.api;

import com.kcy.fitapet.domain.schedule.dto.ScheduleSaveDto;
import com.kcy.fitapet.domain.schedule.service.component.ScheduleManageService;
import com.kcy.fitapet.global.common.response.SuccessResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Tag(name = "스케줄 API", description = "반려동물 일정 관리 API")
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v2/users/{user_id}")
public class ScheduleApi {
    private final ScheduleManageService scheduleManageService;

    @Operation(summary = "스케줄 등록")
    @PostMapping("/pets/{pet_id}/schedules")
    @PreAuthorize("isAuthenticated() and #userId == principal.userId and @managerAuthorize.isManager(#userId, #petId)")
    public ResponseEntity<?> saveSchedule(
            @PathVariable("user_id") Long userId,
            @PathVariable("pet_id") Long petId,
            @RequestBody @Valid ScheduleSaveDto.Request request
    ) {
        scheduleManageService.saveSchedule(petId, request);
        return ResponseEntity.ok(SuccessResponse.noContent());
    }

    @Operation(summary = "pet_id에 속하는 반려동물 스케줄 조회", description = "count가 null이면 전체 조회, null이 아니면 현재 날짜&시간 이후 count 만큼 조회")
    @GetMapping("/pets/{pet_id}/schedules")
    @PreAuthorize("isAuthenticated() and #userId == principal.userId and @managerAuthorize.isManager(#userId, #petId)")
    public ResponseEntity<?> getPetSchedules(
            @PathVariable("user_id") Long userId,
            @PathVariable("pet_id") Long petId,
            @RequestParam(value = "count", required = false) Integer count
    ) {
        return ResponseEntity.ok(SuccessResponse.from(scheduleManageService.findPetSchedules(petId, count)));
    }

    @Operation(summary = "관리 중인 반려동물 날짜별 스케줄 전체 조회")
    @GetMapping("/schedules")
    @PreAuthorize("isAuthenticated() and #userId == principal.userId")
    public ResponseEntity<?> getCalendarSchedules(
            @PathVariable("user_id") Long userId,
            @RequestParam(value = "year") Integer year,
            @RequestParam(value = "month") Integer month,
            @RequestParam(value = "day") Integer day
    ) {
        LocalDateTime date = LocalDate.of(year, month, day).atStartOfDay();
        log.info("date: {}", date);
        return ResponseEntity.ok(SuccessResponse.from(scheduleManageService.findPetSchedules(userId, date)));
    }


}
