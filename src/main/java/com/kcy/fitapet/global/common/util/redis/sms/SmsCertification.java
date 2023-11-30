package com.kcy.fitapet.global.common.util.redis.sms;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@RedisHash(value = "smsCertification", timeToLive = 300)
@Getter
public class SmsCertification {
    @Id
    private final String phoneNumber;
    private final String certificationNumber;

    @Builder
    public SmsCertification(String phoneNumber, String certificationNumber) {
        this.phoneNumber = phoneNumber;
        this.certificationNumber = certificationNumber;
    }

    public static SmsCertification of(String phoneNumber, String certificationNumber) {
        return SmsCertification.builder()
                .phoneNumber(phoneNumber)
                .certificationNumber(certificationNumber)
                .build();
    }
}
