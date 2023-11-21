package com.kcy.fitapet.api.page.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MainPageRes {

    public static class CareSummary {
        private String careName; // careName + (careDetailName)
        private LocalDateTime

    }

}
