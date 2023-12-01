package com.kcy.fitapet.domain.notification.type;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.kcy.fitapet.global.common.util.converter.LegacyCommonType;
import lombok.RequiredArgsConstructor;

import java.util.Map;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toMap;

@RequiredArgsConstructor
public enum NotificationType implements LegacyCommonType {
    NOTICE("1", "공지사항"),
    CARE("2", "케어활동"),
    MEMO("3", "일기"),
    SCHEDULE("4", "스케줄");

    private final String code;
    private final String type;

    private static final Map<String, NotificationType> stringToEnum =
            Stream.of(values()).collect(toMap(Object::toString, e -> e));

    @Override
    public String getCode() { return code; }
    @JsonValue
    public String getType() { return type; }
    @JsonCreator
    public static NotificationType fromString(String type) {
        return stringToEnum.get(type.toUpperCase());
    }
    @Override public String toString() { return type; }
}
