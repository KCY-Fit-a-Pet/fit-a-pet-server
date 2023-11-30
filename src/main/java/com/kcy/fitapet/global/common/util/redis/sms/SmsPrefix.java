package com.kcy.fitapet.global.common.util.redis.sms;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SmsPrefix {
    REGISTER("register@"),
    PASSWORD("password@"),
    UID("uid@");

    private final String prefix;

    public static String getTopic(SmsPrefix prefix, String phoneNumber) {
        return prefix + phoneNumber;
    }
}
