package com.kcy.fitapet.domain.pet.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.kcy.fitapet.domain.care.domain.CareCategory;
import com.kcy.fitapet.domain.pet.domain.Pet;
import com.kcy.fitapet.domain.pet.type.GenderType;
import com.kcy.fitapet.global.common.util.bind.Dto;
import lombok.Getter;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Dto(name = "pet")
public class PetInfoRes {
    private List<?> pets = new ArrayList<>();

    public static PetInfoRes ofSummary(List<Pet> pets) {
        PetInfoRes petInfoRes = new PetInfoRes();
        petInfoRes.pets = pets.stream().map(PetSummaryInfo::of).toList();
        return petInfoRes;
    }

    public static PetInfoRes ofPetInfo(List<Pet> pets) {
        PetInfoRes petInfoRes = new PetInfoRes();
        petInfoRes.pets = pets.stream().map(PetInfo::from).toList();
        return petInfoRes;
    }

    private record PetSummaryInfo(
            Long id,
            String petName,
            String petProfileImage
    ) {
        private static PetSummaryInfo of(Pet pet) {
            return new PetSummaryInfo(
                    pet.getId(),
                    pet.getPetName(),
                    pet.getPetProfileImg()
            );
        }
    }

    private record PetInfo(
            Long id,
            String petName,
            GenderType gender,
            int age,
            String petProfileImage,
            String feed
    ) {
        private static PetInfo from(Pet pet) {
            return new PetInfo(
                    pet.getId(),
                    pet.getPetName(),
                    pet.getGender(),
                    getAmericanAge(pet.getBirthdate()),
                    StringUtils.hasText(pet.getPetProfileImg()) ? pet.getPetProfileImg() : "",
                    StringUtils.hasText(pet.getFeed()) ? pet.getFeed() : ""
            );
        }

        private static int getAmericanAge(LocalDate birthDate) {
            LocalDate now = LocalDate.now();
            int nowYear = now.getYear();
            int nowMonth = now.getMonthValue();
            int nowDay = now.getDayOfMonth();

            int year = birthDate.getYear();
            int month = birthDate.getMonthValue();
            int day = birthDate.getDayOfMonth();

            int age = nowYear - year;
            if (month > nowMonth || (month == nowMonth && day > nowDay)) {
                --age;
            }
            return age;
        }
    }
}
