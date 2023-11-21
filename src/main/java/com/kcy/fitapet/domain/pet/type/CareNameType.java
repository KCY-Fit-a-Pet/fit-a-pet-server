package com.kcy.fitapet.domain.pet.type;

import com.kcy.fitapet.domain.care.type.WeekType;
import lombok.Getter;

@Getter
public enum CareNameType {
    MEAL(WeekType.DAILY),
    WALK(WeekType.WEEKLY),
    CLEAN(WeekType.WEEKLY),
    MEDICINE(WeekType.DAILY),
    SNACK(WeekType.DAILY);

    private final WeekType weekType;

    CareNameType(WeekType weekType) {
        this.weekType = weekType;
    }
}
