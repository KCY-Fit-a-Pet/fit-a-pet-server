package kr.co.fitapet.domain.domains.oauth.type;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import kr.co.fitapet.domain.common.util.converter.LegacyCommonType;
import lombok.RequiredArgsConstructor;

import java.util.Map;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toMap;

@RequiredArgsConstructor
public enum ProviderType implements LegacyCommonType {
    KAKAO("1", "카카오"),
    GOOGLE("2", "구글"),
    NAVER("2", "네이버"),
    APPLE("3", "애플");

    private final String code;
    private final String type;

    private static final Map<String, ProviderType> stringToEnum =
            Stream.of(values()).collect(toMap(Object::toString, e -> e));


    @Override
    public String getCode() {
        return code;
    }

    @JsonValue
    public String getType() {
        return type;
    }

    @JsonCreator
    public static ProviderType fromString(String type) {
        return stringToEnum.get(type.toUpperCase());
    }

    @Override
    public String toString() {
        return name().toLowerCase();
    }
}
