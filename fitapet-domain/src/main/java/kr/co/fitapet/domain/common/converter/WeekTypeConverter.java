package kr.co.fitapet.domain.common.converter;

import jakarta.persistence.Converter;
import kr.co.fitapet.domain.common.util.converter.AbstractLegacyEnumAttributeConverter;
import kr.co.fitapet.domain.domains.care.type.WeekType;

@Converter
public class WeekTypeConverter extends AbstractLegacyEnumAttributeConverter<WeekType> {
    private static final String ENUM_NAME = "돌봄타입";

    public WeekTypeConverter() {
        super(WeekType.class, false, ENUM_NAME);
    }
}
