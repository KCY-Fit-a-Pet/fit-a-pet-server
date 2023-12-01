package com.kcy.fitapet.domain.member.type.converter;

import com.kcy.fitapet.domain.member.type.ManageType;
import com.kcy.fitapet.global.common.util.converter.AbstractLegacyEnumAttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class ManageTypeConverter extends AbstractLegacyEnumAttributeConverter<ManageType> {
    private static final String ENUM_NAME = "관리 멤버 권한";

    public ManageTypeConverter() {
        super(ManageType.class, false, ENUM_NAME);
    }
}
