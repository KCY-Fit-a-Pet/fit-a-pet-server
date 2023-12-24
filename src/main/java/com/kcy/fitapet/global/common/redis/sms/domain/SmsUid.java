package com.kcy.fitapet.global.common.redis.sms.domain;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@RedisHash(value = "smsUid", timeToLive = 300)
@Getter
public class SmsUid {
    @Id
    private final String phoneNumber;
    private final String code;

    @Builder
    public SmsUid(String phoneNumber, String code) {
        this.phoneNumber = phoneNumber;
        this.code = code;
    }

    public static SmsUid of(String phoneNumber, String code) {
        return SmsUid.builder()
                .phoneNumber(phoneNumber)
                .code(code)
                .build();
    }
}
