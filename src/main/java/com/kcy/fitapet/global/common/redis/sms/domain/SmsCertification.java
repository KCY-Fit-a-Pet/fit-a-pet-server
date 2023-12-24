package com.kcy.fitapet.global.common.redis.sms.domain;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@RedisHash(value = "smsCertification", timeToLive = 300)
@Getter
public class SmsCertification {
    @Id
    private final String phoneNumber;
    private final String code;

    @Builder
    public SmsCertification(String phoneNumber, String code) {
        this.phoneNumber = phoneNumber;
        this.code = code;
    }

    public static SmsCertification of(String phoneNumber, String code) {
        return SmsCertification.builder()
                .phoneNumber(phoneNumber)
                .code(code)
                .build();
    }
}
