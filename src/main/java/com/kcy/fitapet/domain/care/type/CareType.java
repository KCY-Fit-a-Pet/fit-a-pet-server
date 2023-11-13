package com.kcy.fitapet.domain.care.type;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.kcy.fitapet.global.common.util.converter.LegacyCommonType;
import lombok.RequiredArgsConstructor;

import java.util.Map;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toMap;

@RequiredArgsConstructor
public enum CareType implements LegacyCommonType {
    DAILY("1", "D"),
    WEEKLY("2", "W");

    private final String code;
    private final String type;

    private static final Map<String, CareType> stringToEnum =
            Stream.of(values()).collect(toMap(Object::toString, e -> e));

    @Override public String getCode() {
        return code;
    }
    @JsonValue public String getType() {
        return type;
    }

    @JsonCreator
    public static CareType fromString(String type) {
        return stringToEnum.get(type.toUpperCase());
    }
    @Override public String toString() {
        return type;
    }
}
