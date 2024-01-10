package com.kcy.fitapet.domain.pet.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.kcy.fitapet.domain.pet.domain.Pet;
import com.kcy.fitapet.global.common.util.bind.Dto;
import lombok.Getter;

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

    @Dto(name = "pet")
    private record PetSummaryInfo(
            String petName,
            String petProfileImage
    ) {
        private static PetSummaryInfo of(Pet pet) {
            return new PetSummaryInfo(
                    pet.getPetName(),
                    pet.getPetProfileImg()
            );
        }
    }
}
