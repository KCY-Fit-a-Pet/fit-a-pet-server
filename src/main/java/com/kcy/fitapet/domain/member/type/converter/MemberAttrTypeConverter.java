package com.kcy.fitapet.domain.member.type.converter;

import com.kcy.fitapet.domain.member.type.MemberAttrType;
import org.springframework.core.convert.converter.Converter;

public class MemberAttrTypeConverter implements Converter<String, MemberAttrType> {
    @Override
    public MemberAttrType convert(String source) {
        try {
            return MemberAttrType.valueOf(source.toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}
