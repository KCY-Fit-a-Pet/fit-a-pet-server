package com.kcy.fitapet.domain.schedule.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
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
            @JsonSerialize(using = LocalTimeSerializer.class)
            @JsonFormat(pattern = "HH:mm:ss")
            @JsonProperty("reservationDate")
            LocalDateTime reservationDate,
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
    }

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
