package kr.co.fitapet.domain.common.redis.sms.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SmsPrefix {
    REGISTER("register"),
    PASSWORD("password"),
    UID("uid"),
    OAUTH("oauth");

    private final String prefix;

    public String getTopic(String phoneNumber) {
        return this.prefix + '@' + phoneNumber;
    }

    @Override
    public String toString() {
        return prefix;
    }
}
