package kr.co.fitapet.api.apis.care.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
import com.vdurmont.emoji.EmojiParser;
import kr.co.fitapet.domain.domains.care.domain.CareDate;
import kr.co.fitapet.domain.domains.care.type.WeekType;
import lombok.Getter;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Getter
public class CareInfoRes {
    private List<?> info = new ArrayList<>();

    public static CareInfoRes from(List<CareCategoryDto> careCategories) {
        CareInfoRes careInfoRes = new CareInfoRes();
        careInfoRes.info = careCategories;
        return careInfoRes;
    }

    public record CareCategoryDto(
        Long careCategoryId,
        String categoryName,
        List<CareDto> cares
    ) {
        public static CareCategoryDto of(Long id, String categoryName, List<CareDto> cares) {
            return new CareCategoryDto(id, EmojiParser.parseToUnicode(categoryName), cares);
        }
    }

    public record CareDto(
            Long careId,
            Long careDateId,
            String careName,
            @JsonSerialize(using = LocalTimeSerializer.class)
            @JsonFormat(pattern = "HH:mm:ss")
            LocalTime careDate,
            boolean isClear
    ) {
        public static CareDto of(Long careId, Long careDateId, String careName, LocalTime careDate, boolean isClear) {
            return new CareDto(careId, careDateId, EmojiParser.parseToUnicode(careName), careDate, isClear);
        }
    }

    public record CareDateDto(
            Long careDateId,
            WeekType week,
            @JsonSerialize(using = LocalTimeSerializer.class)
            @JsonFormat(pattern = "HH:mm:ss")
            LocalTime time
    ) {
        public static CareDateDto fromEntity(CareDate careDate) {
            return new CareDateDto(careDate.getId(), careDate.getWeek(), careDate.getCareTime());
        }
    }
}
