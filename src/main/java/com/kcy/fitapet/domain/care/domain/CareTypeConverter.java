package com.kcy.fitapet.domain.care.domain;

import com.kcy.fitapet.global.common.util.converter.AbstractLegacyEnumAttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class CareTypeConverter extends AbstractLegacyEnumAttributeConverter<CareType> {
    private static final String ENUM_NAME = "돌봄타입";

    public CareTypeConverter() {
        super(CareType.class, false, ENUM_NAME);
    }
}
