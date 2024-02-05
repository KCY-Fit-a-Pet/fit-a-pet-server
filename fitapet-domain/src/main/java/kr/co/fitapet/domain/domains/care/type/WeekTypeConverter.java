package kr.co.fitapet.domain.domains.care.type;

import com.kcy.fitapet.global.common.util.converter.AbstractLegacyEnumAttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class WeekTypeConverter extends AbstractLegacyEnumAttributeConverter<WeekType> {
    private static final String ENUM_NAME = "돌봄타입";

    public WeekTypeConverter() {
        super(WeekType.class, false, ENUM_NAME);
    }
}
