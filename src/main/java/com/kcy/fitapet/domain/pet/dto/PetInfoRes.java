package com.kcy.fitapet.domain.pet.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.kcy.fitapet.domain.care.domain.CareCategory;
import com.kcy.fitapet.domain.pet.domain.Pet;
import com.kcy.fitapet.domain.pet.type.GenderType;
import com.kcy.fitapet.global.common.util.bind.Dto;
import lombok.Getter;
import org.springframework.util.StringUtils;

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
            String petProfileImage,
            String feed,
            List<Long> careIds
    ) {
        private static PetInfo from(Pet pet) {
            return new PetInfo(
                    pet.getId(),
                    pet.getPetName(),
                    pet.getGender(),
                    StringUtils.hasText(pet.getPetProfileImg()) ? pet.getPetProfileImg() : "",
                    StringUtils.hasText(pet.getFeed()) ? pet.getFeed() : "",
                    pet.getCareCategories().stream().map(CareCategory::getId).toList()
            );
        }
    }
}
