package kr.co.fitapet.domain.common.converter;

import jakarta.persistence.Converter;
import kr.co.fitapet.domain.common.util.converter.AbstractLegacyEnumAttributeConverter;
import kr.co.fitapet.domain.domains.member.type.ManageType;

@Converter
public class ManageTypeConverter extends AbstractLegacyEnumAttributeConverter<ManageType> {
    private static final String ENUM_NAME = "관리 멤버 권한";

    public ManageTypeConverter() {
        super(ManageType.class, false, ENUM_NAME);
    }
}
