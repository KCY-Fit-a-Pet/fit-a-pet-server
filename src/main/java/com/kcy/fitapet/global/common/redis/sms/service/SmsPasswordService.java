package com.kcy.fitapet.global.common.redis.sms.service;

import com.kcy.fitapet.global.common.redis.sms.provider.SmsRedisProvider;
import com.kcy.fitapet.global.common.redis.sms.qualify.SmsPasswordQualifier;
import com.kcy.fitapet.global.common.redis.sms.type.SmsPrefix;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class SmsPasswordService {
    private final SmsRedisProvider smsRedisProvider;

    public SmsPasswordService(@SmsPasswordQualifier final SmsRedisProvider smsRedisProvider) {
        this.smsRedisProvider = smsRedisProvider;
    }

    public void save(String phone, String code, SmsPrefix prefix) {
        smsRedisProvider.saveSmsAuthToken(phone, code, prefix);
    }

    public boolean isCorrectCode(String phone, String code, SmsPrefix prefix) {
        return smsRedisProvider.isCorrectCode(phone, code, prefix);
    }

    public boolean isExistsCode(String phone, SmsPrefix prefix) {
        return smsRedisProvider.isExistsCode(phone, prefix);
    }

    public void removeCode(String phone, SmsPrefix prefix) {
        smsRedisProvider.removeCode(phone, prefix);
    }

    public LocalDateTime getExpiredTime(String phone, SmsPrefix prefix) {
        return smsRedisProvider.getExpiredTime(phone, prefix);
    }
}
