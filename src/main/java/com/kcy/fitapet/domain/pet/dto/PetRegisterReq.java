package com.kcy.fitapet.domain.pet.dto;

import com.kcy.fitapet.domain.pet.type.GenderType;

import java.time.LocalDate;

public class PetRegisterReq {
    private String petName;
    private String species;
    private GenderType gender;
    private boolean neutralization;
    private LocalDate birthDate;


}
