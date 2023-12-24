package com.kcy.fitapet.global.common.redis.sms.domain;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@RedisHash(value = "smsRegister", timeToLive = 300)
@Getter
public class SmsRegister {
    @Id
    private final String phoneNumber;
    private final String code;

    @Builder
    public SmsRegister(String phoneNumber, String code) {
        this.phoneNumber = phoneNumber;
        this.code = code;
    }

    public static SmsRegister of(String phoneNumber, String code) {
        return SmsRegister.builder()
                .phoneNumber(phoneNumber)
                .code(code)
                .build();
    }
}
