package com.kcy.fitapet.domain.schedule.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.kcy.fitapet.domain.pet.domain.Pet;
import com.kcy.fitapet.domain.schedule.domain.Schedule;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class ScheduleInfoDto {
    private List<?> schedules;

    public static ScheduleInfoDto of(List<ScheduleInfo> schedules) {
        ScheduleInfoDto scheduleInfoDto = new ScheduleInfoDto();
        scheduleInfoDto.schedules = schedules;
        return scheduleInfoDto;
    }

    public record ScheduleInfo(
            Long scheduleId,
            String scheduleName,
            String location,
            @JsonSerialize(using = LocalDateTimeSerializer.class)
            @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
            @JsonProperty("reservationDate")
            LocalDateTime reservationDt,
            @JsonInclude(JsonInclude.Include.NON_EMPTY)
            List<ParticipantPetInfo> pets
    ) {
        public static ScheduleInfo from(Schedule schedule, List<Pet> pets) {
            return new ScheduleInfo(
                    schedule.getId(),
                    schedule.getScheduleName(),
                    schedule.getLocation(),
                    schedule.getReservationDt(),
                    pets.stream()
                            .map(ParticipantPetInfo::from)
                            .toList()
            );
        }

        public static ScheduleInfo valueOf(ScheduleQueryDslRes queryDslRes, List<Pet> pets) {
            return new ScheduleInfo(
                    queryDslRes.scheduleId(),
                    queryDslRes.scheduleName(),
                    queryDslRes.location(),
                    queryDslRes.reservationDt(),
                    queryDslRes.petIds().stream()
                            .map(petId -> pets.stream()
                                    .filter(pet -> pet.getId().equals(petId))
                                    .findFirst()
                                    .map(ParticipantPetInfo::from)
                                    .orElseThrow(() -> new IllegalArgumentException("해당 반려동물이 존재하지 않습니다.")))
                            .toList()
            );
        }
    }

    /**
     * QueryDsl로 조회한 스케줄 정보 <br/>
     * Query 최적화를 위한 목적의 DTO이며, 클라이언트 응답에는 ScheduleInfo로 변경하여 응답한다.
     * @param scheduleId
     * @param scheduleName
     * @param location
     * @param reservationDt
     * @param petIds
     */
    public record ScheduleQueryDslRes(
            Long scheduleId,
            String scheduleName,
            String location,
            LocalDateTime reservationDt,
            List<Long> petIds
    ) {
    }

    /**
     * 스케줄 참여 반려동물 정보
     * @param petId
     * @param petProfileImage
     */
    public record ParticipantPetInfo(
            Long petId,
            String petProfileImage
    ) {
        public static ParticipantPetInfo from(Pet pet) {
            return new ParticipantPetInfo(
                    pet.getId(),
                    pet.getPetProfileImg()
            );
        }
    }


}
