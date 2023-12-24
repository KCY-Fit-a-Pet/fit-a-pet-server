package com.kcy.fitapet.global.common.redis.sms.domain;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@RedisHash(value = "smsPassword", timeToLive = 300)
@Getter
public class SmsPassword {
    @Id
    private final String phoneNumber;
    private final String code;

    @Builder
    public SmsPassword(String phoneNumber, String code) {
        this.phoneNumber = phoneNumber;
        this.code = code;
    }

    public static SmsPassword of(String phoneNumber, String code) {
        return SmsPassword.builder()
                .phoneNumber(phoneNumber)
                .code(code)
                .build();
    }
}
