package kr.co.fitapet.domain.common.redis.sms.provider;

import kr.co.fitapet.domain.common.redis.exception.RedisErrorCode;
import kr.co.fitapet.domain.common.redis.exception.RedisErrorException;
import kr.co.fitapet.domain.common.redis.sms.dao.SmsRegisterRepository;
import kr.co.fitapet.domain.common.redis.sms.domain.SmsRegister;
import kr.co.fitapet.domain.common.redis.sms.qualify.SmsRegisterQualifier;
import kr.co.fitapet.domain.common.redis.sms.type.SmsPrefix;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Slf4j
@Service
@SmsRegisterQualifier
@RequiredArgsConstructor
public class SmsRegisterProvider implements SmsRedisProvider {
    private final SmsRegisterRepository smsRegisterRepository;
    private final RedisTemplate<String, SmsRegister> redisTemplate;

    @Override
    public void saveSmsAuthToken(String phone, String code, SmsPrefix prefix) {
        smsRegisterRepository.save(SmsRegister.of(phone, code));
    }

    @Override
    public boolean isCorrectCode(String phoneNumber, String code, SmsPrefix prefix) {
        Optional<SmsRegister> smsRegister = smsRegisterRepository.findById(phoneNumber);
        return smsRegister.map(register -> register.getCode().equals(code)).orElse(false);
    }

    @Override
    public boolean isExistsCode(String phoneNumber, SmsPrefix prefix) {
        return smsRegisterRepository.existsById(phoneNumber);
    }

    @Override
    public void removeCode(String phoneNumber, SmsPrefix prefix) {
        smsRegisterRepository.deleteById(phoneNumber);
    }

    @Override
    public LocalDateTime getExpiredTime(String phoneNumber, SmsPrefix prefix) {
        Long ttl = redisTemplate.getExpire(getTopic(phoneNumber, prefix));
        log.info("ttl: {}", ttl);

        if (ttl == null || ttl < 0L)
            throw new RedisErrorException(RedisErrorCode.EXPIRED_VALUE);

        return LocalDateTime.now().plusSeconds(ttl);
    }
}
