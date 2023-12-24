package com.kcy.fitapet.global.common.redis.sms;

import com.kcy.fitapet.global.common.redis.sms.service.SmsOauthService;
import com.kcy.fitapet.global.common.redis.sms.service.SmsPasswordService;
import com.kcy.fitapet.global.common.redis.sms.service.SmsRegisterService;
import com.kcy.fitapet.global.common.redis.sms.service.SmsUidService;
import com.kcy.fitapet.global.common.redis.sms.type.SmsPrefix;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class SmsRedisHelper {
    private final SmsOauthService smsOauthService;
    private final SmsRegisterService smsRegisterService;
    private final SmsPasswordService smsPasswordService;
    private final SmsUidService smsUidService;

    public void saveSmsAuthToken(String phone, String code, SmsPrefix prefix) {
        switch (prefix) {
            case OAUTH -> smsOauthService.save(phone, code, prefix);
            case REGISTER -> smsRegisterService.save(phone, code, prefix);
            case PASSWORD -> smsPasswordService.save(phone, code, prefix);
            case UID -> smsUidService.save(phone, code, prefix);
        }
    }

    public boolean isCorrectCode(String phone, String code, SmsPrefix prefix) {
        return switch (prefix) {
            case OAUTH -> smsOauthService.isCorrectCode(phone, code, prefix);
            case REGISTER -> smsRegisterService.isCorrectCode(phone, code, prefix);
            case PASSWORD -> smsPasswordService.isCorrectCode(phone, code, prefix);
            case UID -> smsUidService.isCorrectCode(phone, code, prefix);
        };
    }

    public boolean isExistsCode(String phone, SmsPrefix prefix) {
        return switch (prefix) {
            case OAUTH -> smsOauthService.isExistsCode(phone, prefix);
            case REGISTER -> smsRegisterService.isExistsCode(phone, prefix);
            case PASSWORD -> smsPasswordService.isExistsCode(phone, prefix);
            case UID -> smsUidService.isExistsCode(phone, prefix);
        };
    }

    public void removeCode(String phone, SmsPrefix prefix) {
        switch (prefix) {
            case OAUTH -> smsOauthService.removeCode(phone, prefix);
            case REGISTER -> smsRegisterService.removeCode(phone, prefix);
            case PASSWORD -> smsPasswordService.removeCode(phone, prefix);
            case UID -> smsUidService.removeCode(phone, prefix);
        };
    }

    public LocalDateTime getExpiredTime(String phone, SmsPrefix prefix) {
        return switch (prefix) {
            case OAUTH -> smsOauthService.getExpiredTime(phone, prefix);
            case REGISTER -> smsRegisterService.getExpiredTime(phone, prefix);
            case PASSWORD -> smsPasswordService.getExpiredTime(phone, prefix);
            case UID -> smsUidService.getExpiredTime(phone, prefix);
        };
    }
}
