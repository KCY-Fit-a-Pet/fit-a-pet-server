package kr.co.fitapet.domain.common.redis.sms.provider;

import kr.co.fitapet.domain.common.redis.exception.RedisErrorCode;
import kr.co.fitapet.domain.common.redis.exception.RedisErrorException;
import kr.co.fitapet.domain.common.redis.sms.dao.SmsOauthRepository;
import kr.co.fitapet.domain.common.redis.sms.domain.SmsOauth;
import kr.co.fitapet.domain.common.redis.sms.qualify.SmsOauthQualifier;
import kr.co.fitapet.domain.common.redis.sms.type.SmsPrefix;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Slf4j
@Service
@Primary
@RequiredArgsConstructor
@SmsOauthQualifier
public class SmsOauthProvider implements SmsRedisProvider {
    private final SmsOauthRepository smsOauthRepository;

    private final RedisTemplate<String, SmsOauth> redisTemplate;

    @Override
    public void saveSmsAuthToken(String phone, String code, SmsPrefix prefix) {
        smsOauthRepository.save(SmsOauth.of(phone, code));
    }

    @Override
    public boolean isCorrectCode(String phoneNumber, String code, SmsPrefix prefix) {
        log.info("phoneNumber: {}, code: {}", phoneNumber, code);
        Optional<SmsOauth> smsOauth = smsOauthRepository.findById(phoneNumber);
        log.info("smsOauth: {}", smsOauth.get().getCode());
        return smsOauth.map(oauth -> oauth.getCode().equals(code)).orElse(false);
    }

    @Override
    public boolean isExistsCode(String phoneNumber, SmsPrefix prefix) {
        return smsOauthRepository.existsById(phoneNumber);
    }

    @Override
    public void removeCode(String phoneNumber, SmsPrefix prefix) {
        smsOauthRepository.deleteById(phoneNumber);
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
