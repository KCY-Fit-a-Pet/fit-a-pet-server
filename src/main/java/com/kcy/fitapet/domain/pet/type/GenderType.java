package com.kcy.fitapet.domain.pet.type;

import com.kcy.fitapet.global.common.util.converter.LegacyCommonType;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum GenderType implements LegacyCommonType {
    MALE("1", "수컷"),
    FEMALE("2", "암컷");

    private final String code;
    private final String gender;

    @Override
    public String getCode() {
        return null;
    }
}
