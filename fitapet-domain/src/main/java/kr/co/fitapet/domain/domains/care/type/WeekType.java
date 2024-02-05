package kr.co.fitapet.domain.domains.care.type;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.kcy.fitapet.global.common.util.converter.LegacyCommonType;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.Map;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toMap;

@RequiredArgsConstructor
public enum WeekType implements LegacyCommonType {
    MON("1", "월", "MONDAY"),
    TUE("2", "화", "TUESDAY"),
    WED("3", "수", "WEDNESDAY"),
    THU("4", "목", "THURSDAY"),
    FRI("5", "금", "FRIDAY"),
    SAT("6", "토", "SATURDAY"),
    SUN("7", "일", "SUNDAY");

    private final String code;
    private final String type;
    private final String legacyType;

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
    public static WeekType fromLegacyType(String legacyType) {
        return Stream.of(values())
                .filter(weekType -> weekType.legacyType.equals(legacyType.toUpperCase()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No such type: " + legacyType));
    }
    @Override public String toString() {
        return type;
    }

    public boolean checkToday() {
        return this.equals(fromLegacyType(LocalDate.now().getDayOfWeek().toString()));
    }
}
