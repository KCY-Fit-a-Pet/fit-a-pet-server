package com.kcy.fitapet.domain.schedule.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
import com.kcy.fitapet.domain.schedule.domain.Schedule;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.List;

public class ScheduleSaveDto {
    @Schema(description = "스케줄 등록 요청")
    public record Request(
            @Schema(description = "스케줄 이름", example = "하루 병원 정기 검진", requiredMode = Schema.RequiredMode.REQUIRED)
            @NotEmpty(message = "스케줄 이름은 필수입니다.")
            String scheduleName,
            @Schema(description = "장소", example = "oo 병원", requiredMode = Schema.RequiredMode.REQUIRED)
            @NotNull
            String location,
            @Schema(description = "예약 날짜", example = "2021-10-10 10:10:10", requiredMode = Schema.RequiredMode.REQUIRED)
            @JsonSerialize(using = LocalTimeSerializer.class)
            @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
            @NotNull(message = "예약 날짜는 필수입니다.")
            LocalDateTime reservationDate,
            @Schema(description = "알림 시간(분 단위)", example = "30 (단, 없으면 0)", requiredMode = Schema.RequiredMode.REQUIRED)
            @NotNull
            Integer notifyTime,
            @Schema(description = "케어 동물 추가", example = "[1, 2, 3]", requiredMode = Schema.RequiredMode.REQUIRED)
            @NotEmpty
            List<Long> petIds
    ) {
        public Schedule toEntity() {
            return Schedule.of(scheduleName, location, reservationDate, reservationDate.plusMinutes(notifyTime));
        }
    }
}
