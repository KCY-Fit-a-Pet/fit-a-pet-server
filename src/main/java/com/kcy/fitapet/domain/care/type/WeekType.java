package com.kcy.fitapet.domain.care.type;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.kcy.fitapet.global.common.util.converter.LegacyCommonType;
import lombok.RequiredArgsConstructor;

import java.util.Map;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toMap;

@RequiredArgsConstructor
public enum WeekType implements LegacyCommonType {
    MON("1", "월"),
    TUE("2", "화"),
    WED("3", "수"),
    THU("4", "목"),
    FRI("5", "금"),
    SAT("6", "토"),
    SUN("7", "일");

    private final String code;
    private final String type;

    private static final Map<String, WeekType> stringToEnum =
            Stream.of(values()).collect(toMap(Object::toString, e -> e));

    @Override public String getCode() {
        return code;
    }
    @JsonValue public String getType() {
        return type;
    }

    @JsonCreator
    public static WeekType fromString(String type) {
        return stringToEnum.get(type.toUpperCase());
    }
    @Override public String toString() {
        return type;
    }
}
