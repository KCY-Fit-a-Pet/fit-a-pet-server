package com.kcy.fitapet.domain.member.type;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.kcy.fitapet.global.common.util.converter.LegacyCommonType;
import lombok.RequiredArgsConstructor;

import java.util.Map;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toMap;

@RequiredArgsConstructor
public enum ManageType implements LegacyCommonType {
    MASTER("1"), MANAGER("2");

    private final String code;

    @Override
    public String getCode() {
        return code;
    }

    @JsonCreator
    public static ManageType from(String s) {
        return Stream.of(values()).filter(v -> v.name().equals(s.toUpperCase())).findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }

    @Override public String toString() { return name(); }
}
