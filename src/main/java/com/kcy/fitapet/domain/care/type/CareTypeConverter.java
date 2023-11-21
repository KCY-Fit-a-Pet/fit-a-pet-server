package com.kcy.fitapet.domain.care.type;

import com.kcy.fitapet.global.common.util.converter.AbstractLegacyEnumAttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class CareTypeConverter extends AbstractLegacyEnumAttributeConverter<WeekType> {
    private static final String ENUM_NAME = "돌봄타입";

    public CareTypeConverter() {
        super(WeekType.class, false, ENUM_NAME);
    }
}
