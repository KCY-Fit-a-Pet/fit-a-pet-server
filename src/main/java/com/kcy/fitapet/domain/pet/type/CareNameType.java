package com.kcy.fitapet.domain.pet.type;

import com.kcy.fitapet.domain.care.type.CareType;
import lombok.Getter;

@Getter
public enum CareNameType {
    MEAL(CareType.DAILY),
    WALK(CareType.WEEKLY),
    CLEAN(CareType.WEEKLY),
    MEDICINE(CareType.DAILY),
    SNACK(CareType.DAILY);

    private CareType careType;

    CareNameType(CareType careType) {
        this.careType = careType;
    }
}
