package kr.co.fitapet.domain.common.redis.sms.provider;

import kr.co.fitapet.domain.common.redis.exception.RedisErrorCode;
import kr.co.fitapet.domain.common.redis.exception.RedisErrorException;
import kr.co.fitapet.domain.common.redis.sms.dao.SmsUidRepository;
import kr.co.fitapet.domain.common.redis.sms.domain.SmsUid;
import kr.co.fitapet.domain.common.redis.sms.qualify.SmsUidQualifier;
import kr.co.fitapet.domain.common.redis.sms.type.SmsPrefix;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Slf4j
@Service
@SmsUidQualifier
@RequiredArgsConstructor
public class SmsUidProvider implements SmsRedisProvider {
    private final SmsUidRepository smsUidRepository;
    private final RedisTemplate<String, SmsUid> redisTemplate;

    @Override
    public void saveSmsAuthToken(String phone, String code, SmsPrefix prefix) {
        smsUidRepository.save(SmsUid.of(phone, code));
    }

    @Override
    public boolean isCorrectCode(String phoneNumber, String code, SmsPrefix prefix) {
        Optional<SmsUid> smsUid = smsUidRepository.findById(phoneNumber);
        return smsUid.map(uid -> uid.getCode().equals(code)).orElse(false);
    }

    @Override
    public boolean isExistsCode(String phoneNumber, SmsPrefix prefix) {
        return smsUidRepository.existsById(phoneNumber);
    }

    @Override
    public void removeCode(String phoneNumber, SmsPrefix prefix) {
        smsUidRepository.deleteById(phoneNumber);
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
