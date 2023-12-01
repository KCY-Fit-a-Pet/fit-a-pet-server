package com.kcy.fitapet.global.common.redis.sms;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SmsPrefix {
    REGISTER("register@"),
    PASSWORD("password@"),
    UID("uid@");

    private final String prefix;

    public String getTopic(String phoneNumber) {
        return this.prefix + phoneNumber;
    }

    @Override
    public String toString() {
        return prefix;
    }
}
