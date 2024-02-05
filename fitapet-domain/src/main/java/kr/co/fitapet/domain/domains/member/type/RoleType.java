package kr.co.fitapet.domain.domains.member.type;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.kcy.fitapet.global.common.util.converter.LegacyCommonType;
import lombok.RequiredArgsConstructor;

import java.util.Map;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toMap;

@RequiredArgsConstructor
public enum RoleType implements LegacyCommonType {
    ADMIN("1", "ROLE_ADMIN"),
    USER("2", "ROLE_USER");

    private final String code;
    private final String role;
    private static final Map<String, RoleType> stringToEnum =
            Stream.of(values()).collect(toMap(Object::toString, e -> e));

    @JsonValue
    public String getRole() { return role; }
    @Override
    public String getCode() { return code; }

    @JsonCreator
    public static RoleType fromString(String role) {
        return stringToEnum.get(role.toUpperCase());
    }

    @Override public String toString() { return role; }
}
