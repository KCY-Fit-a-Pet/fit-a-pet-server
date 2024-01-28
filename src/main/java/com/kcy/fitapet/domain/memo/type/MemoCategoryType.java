package com.kcy.fitapet.domain.memo.type;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum MemoCategoryType {
    ROOT, SUB;

    @JsonCreator
    public static MemoCategoryType from(String s) {
        return MemoCategoryType.valueOf(s.toUpperCase());
    }
}
