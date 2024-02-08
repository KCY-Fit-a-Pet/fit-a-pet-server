package kr.co.fitapet.domain.domains.pet.type;

import com.fasterxml.jackson.annotation.JsonCreator;
import kr.co.fitapet.domain.common.util.converter.LegacyCommonType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum GenderType implements LegacyCommonType {
    MALE("1", "수컷"),
    FEMALE("2", "암컷");

    private final String code;
    private final String value;

    @JsonCreator
    public static GenderType from(String gender) {
        return GenderType.valueOf(gender.toUpperCase());
    }
}
