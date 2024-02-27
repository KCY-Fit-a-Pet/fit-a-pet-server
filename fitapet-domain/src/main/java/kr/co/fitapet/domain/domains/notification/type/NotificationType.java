package kr.co.fitapet.domain.domains.notification.type;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import kr.co.fitapet.domain.common.util.converter.LegacyCommonType;
import lombok.RequiredArgsConstructor;

import java.util.Map;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toMap;

@RequiredArgsConstructor
public enum NotificationType implements LegacyCommonType {
    NOTICE("1", "공지사항"),
    MEMBER("2", "멤버"),
    MANAGER("3", "매니저 활동"),
    PET("4", "반려동물"),
    CARE("5", "케어활동"),
    MEMO("6", "일기"),
    SCHEDULE("7", "스케줄");

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
    @Override public String toString() { return name().toLowerCase(); }
}
