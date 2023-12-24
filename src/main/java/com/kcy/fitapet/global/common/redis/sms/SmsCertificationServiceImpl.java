package com.kcy.fitapet.global.common.redis.sms;

import com.kcy.fitapet.domain.member.exception.SmsErrorCode;
import com.kcy.fitapet.global.common.redis.sms.dao.*;
import com.kcy.fitapet.global.common.redis.sms.domain.*;
import com.kcy.fitapet.global.common.response.exception.GlobalErrorException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class SmsCertificationServiceImpl implements SmsCertificationService {
    private final SmsOauthRepository smsOauthRepository;
    private final SmsPasswordRepository smsPasswordRepository;
    private final SmsRegisterRepository smsRegisterRepository;
    private final SmsUidRepository smsUidRepository;

    private final RedisTemplate<String, SmsCertification> redisTemplate;

    @Override
    public void saveSmsAuthToken(String phone, String code, SmsPrefix prefix) {
        switch (prefix) {
            case OAUTH -> smsOauthRepository.save(SmsOauth.of(phone, code));
            case PASSWORD -> smsPasswordRepository.save(SmsPassword.of(phone, code));
            case REGISTER -> smsRegisterRepository.save(SmsRegister.of(phone, code));
            case UID -> smsUidRepository.save(SmsUid.of(phone, code));
        }
    }

    @Override
    public boolean isCorrectCode(String phoneNumber, String code, SmsPrefix prefix) {
        switch (prefix) {
            case OAUTH -> {
                Optional<SmsOauth> smsOauth = smsOauthRepository.findById(phoneNumber);
                return smsOauth.map(oauth -> oauth.getCode().equals(code)).orElse(false);
            }
            case PASSWORD -> {
                Optional<SmsPassword> smsPassword = smsPasswordRepository.findById(phoneNumber);
                return smsPassword.map(password -> password.getCode().equals(code)).orElse(false);
            }
            case REGISTER -> {
                Optional<SmsRegister> smsRegister = smsRegisterRepository.findById(phoneNumber);
                return smsRegister.map(register -> register.getCode().equals(code)).orElse(false);
            }
            case UID -> {
                Optional<SmsUid> smsUid = smsUidRepository.findById(phoneNumber);
                return smsUid.map(uid -> uid.getCode().equals(code)).orElse(false);
            }
            default -> throw new GlobalErrorException(SmsErrorCode.NOT_FOUND_SMS_PREFIX);
        }
    }

    @Override
    public boolean existsCode(String phoneNumber, SmsPrefix prefix) {
        switch (prefix) {
            case OAUTH -> {return smsOauthRepository.existsById(phoneNumber);}
            case PASSWORD -> {return smsPasswordRepository.existsById(phoneNumber);}
            case REGISTER -> {return smsRegisterRepository.existsById(phoneNumber);}
            case UID -> {return smsUidRepository.existsById(phoneNumber);}
            default -> throw new GlobalErrorException(SmsErrorCode.NOT_FOUND_SMS_PREFIX);
        }
    }

    @Override
    public void removeCode(String phoneNumber, SmsPrefix prefix) {
        switch (prefix) {
            case OAUTH -> {smsOauthRepository.deleteById(phoneNumber);}
            case PASSWORD -> {smsPasswordRepository.deleteById(phoneNumber);}
            case REGISTER -> {smsRegisterRepository.deleteById(phoneNumber);}
            case UID -> {smsUidRepository.deleteById(phoneNumber);}
            default -> throw new GlobalErrorException(SmsErrorCode.NOT_FOUND_SMS_PREFIX);
        }
    }

    @Override
    public LocalDateTime getExpiredTime(String phoneNumber, SmsPrefix prefix) {
        Long ttl = redisTemplate.getExpire(getKeyName(phoneNumber, prefix));
        log.info("ttl: {}", ttl);

        if (ttl == null || ttl < 0L)
            throw new GlobalErrorException(SmsErrorCode.EXPIRED_AUTH_CODE);

        return LocalDateTime.now().plusSeconds(ttl);
    }

    private String getKeyName(String phoneNumber, SmsPrefix prefix) {
        String str = prefix.getPrefix();
        return "sms" + str.substring(0, 1).toUpperCase()
                + str.substring(1) + ":" + phoneNumber;
    }
}
