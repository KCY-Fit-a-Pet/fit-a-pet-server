package kr.co.fitapet.domain.common.converter;

import jakarta.persistence.Converter;
import kr.co.fitapet.domain.common.util.converter.AbstractLegacyEnumAttributeConverter;
import kr.co.fitapet.domain.domains.member.type.RoleType;

@Converter
public class RoleTypeConverter extends AbstractLegacyEnumAttributeConverter<RoleType> {
    private static final String ENUM_NAME = "유저권한";

    public RoleTypeConverter() {
        super(RoleType.class, false, ENUM_NAME);
    }
}