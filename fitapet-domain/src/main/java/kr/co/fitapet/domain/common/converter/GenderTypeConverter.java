package kr.co.fitapet.domain.common.converter;

import jakarta.persistence.Converter;
import kr.co.fitapet.domain.common.util.converter.AbstractLegacyEnumAttributeConverter;
import kr.co.fitapet.domain.domains.pet.type.GenderType;

@Converter
public class GenderTypeConverter extends AbstractLegacyEnumAttributeConverter<GenderType> {
    private static final String ENUM_NAME = "성별";

    public GenderTypeConverter() {
        super(GenderType.class, false, ENUM_NAME);
    }
}
