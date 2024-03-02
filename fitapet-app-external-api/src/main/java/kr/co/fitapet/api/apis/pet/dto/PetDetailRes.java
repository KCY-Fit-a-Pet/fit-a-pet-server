package kr.co.fitapet.api.apis.pet.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import kr.co.fitapet.domain.domains.pet.domain.Pet;
import kr.co.fitapet.domain.domains.pet.type.GenderType;

import java.time.LocalDate;
import java.util.Objects;

public record PetDetailRes(
        Long id,
        String petName,
        String petProfileImage,
        GenderType gender,
        Boolean neutered,
        @JsonSerialize(using = LocalDateSerializer.class)
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
        LocalDate birthdate,
        String species,
        String feed
) {
    public static PetDetailRes from(Pet pet) {
        return new PetDetailRes(
                pet.getId(),
                pet.getPetName(),
                Objects.toString(pet.getPetProfileImg(), ""),
                pet.getGender(),
                pet.isNeutered(),
                pet.getBirthdate(),
                pet.getSpecies(),
                Objects.toString(pet.getFeed(), "")
        );
    }
}
