package kr.co.fitapet.api.apis.auth.mapper;

import kr.co.fitapet.domain.common.redis.sms.provider.SmsRedisProvider;
import kr.co.fitapet.domain.common.redis.sms.qualify.SmsOauthQualifier;
import kr.co.fitapet.domain.common.redis.sms.qualify.SmsPasswordQualifier;
import kr.co.fitapet.domain.common.redis.sms.qualify.SmsRegisterQualifier;
import kr.co.fitapet.domain.common.redis.sms.qualify.SmsUidQualifier;
import kr.co.fitapet.domain.common.redis.sms.type.SmsPrefix;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Map;

@Component
public class SmsRedisMapper {
    private final Map<SmsPrefix, SmsRedisProvider> smsProviderMap;

    public SmsRedisMapper(
            @SmsOauthQualifier SmsRedisProvider smsOauthService,
            @SmsRegisterQualifier SmsRedisProvider smsRegisterService,
            @SmsPasswordQualifier SmsRedisProvider smsPasswordService,
            @SmsUidQualifier SmsRedisProvider smsUidService
    ) {
        smsProviderMap = Map.of(
                SmsPrefix.OAUTH, smsOauthService,
                SmsPrefix.REGISTER, smsRegisterService,
                SmsPrefix.PASSWORD, smsPasswordService,
                SmsPrefix.UID, smsUidService
        );
    }

    public void saveSmsAuthToken(String phone, String code, SmsPrefix prefix) {
        smsProviderMap.get(prefix).saveSmsAuthToken(phone, code, prefix);
    }

    public boolean isCorrectCode(String phone, String code, SmsPrefix prefix) {
        return smsProviderMap.get(prefix).isCorrectCode(phone, code, prefix);
    }

    public boolean isExistsCode(String phone, SmsPrefix prefix) {
        return smsProviderMap.get(prefix).isExistsCode(phone, prefix);
    }

    public void removeCode(String phone, SmsPrefix prefix) {
        smsProviderMap.get(prefix).removeCode(phone, prefix);
    }

    public LocalDateTime getExpiredTime(String phone, SmsPrefix prefix) {
        return smsProviderMap.get(prefix).getExpiredTime(phone, prefix);
    }
}
