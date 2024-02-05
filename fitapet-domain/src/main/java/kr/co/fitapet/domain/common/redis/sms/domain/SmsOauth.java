package kr.co.fitapet.domain.common.redis.sms.domain;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@RedisHash(value = "smsOauth", timeToLive = 300)
@Getter
public class SmsOauth {
    @Id
    private final String phoneNumber;
    private final String code;

    @Builder
    public SmsOauth(String phoneNumber, String code) {
        this.phoneNumber = phoneNumber;
        this.code = code;
    }

    public static SmsOauth of(String phoneNumber, String code) {
        return SmsOauth.builder()
                .phoneNumber(phoneNumber)
                .code(code)
                .build();
    }
}
