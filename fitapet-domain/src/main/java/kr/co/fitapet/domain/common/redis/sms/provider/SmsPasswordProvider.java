package kr.co.fitapet.domain.common.redis.sms.provider;

import kr.co.fitapet.domain.common.redis.exception.RedisErrorCode;
import kr.co.fitapet.domain.common.redis.exception.RedisErrorException;
import kr.co.fitapet.domain.common.redis.sms.dao.SmsPasswordRepository;
import kr.co.fitapet.domain.common.redis.sms.domain.SmsPassword;
import kr.co.fitapet.domain.common.redis.sms.qualify.SmsPasswordQualifier;
import kr.co.fitapet.domain.common.redis.sms.type.SmsPrefix;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Optional;

@Slf4j
@Component
@SmsPasswordQualifier
@RequiredArgsConstructor
public class SmsPasswordProvider implements SmsRedisProvider {
    private final SmsPasswordRepository smsPasswordRepository;
    private final RedisTemplate<String, SmsPassword> restTemplate;

    @Override
    public void saveSmsAuthToken(String phone, String code, SmsPrefix prefix) {
        smsPasswordRepository.save(SmsPassword.of(phone, code));
    }

    @Override
    public boolean isCorrectCode(String phoneNumber, String code, SmsPrefix prefix) {
        Optional<SmsPassword> smsPassword = smsPasswordRepository.findById(phoneNumber);
        return smsPassword.map(password -> password.getCode().equals(code)).orElse(false);
    }

    @Override
    public boolean isExistsCode(String phoneNumber, SmsPrefix prefix) {
        return smsPasswordRepository.existsById(phoneNumber);
    }

    @Override
    public void removeCode(String phoneNumber, SmsPrefix prefix) {
        smsPasswordRepository.deleteById(phoneNumber);
    }

    @Override
    public LocalDateTime getExpiredTime(String phoneNumber, SmsPrefix prefix) {
        Long ttl = restTemplate.getExpire(getTopic(phoneNumber, prefix));
        log.info("ttl: {}", ttl);

        if (ttl == null || ttl < 0L)
            throw new RedisErrorException(RedisErrorCode.EXPIRED_VALUE);

        return LocalDateTime.now().plusSeconds(ttl);
    }
}
