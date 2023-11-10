package com.kcy.fitapet.domain.pet.type;

import com.kcy.fitapet.global.common.util.converter.AbstractLegacyEnumAttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class GenderTypeConverter extends AbstractLegacyEnumAttributeConverter<GenderType> {
    private static final String ENUM_NAME = "성별";

    public GenderTypeConverter() {
        super(GenderType.class, false, ENUM_NAME);
    }
}
