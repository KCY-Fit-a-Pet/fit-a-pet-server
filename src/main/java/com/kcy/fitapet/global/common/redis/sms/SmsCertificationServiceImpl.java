package com.kcy.fitapet.global.common.redis.sms;

import com.kcy.fitapet.domain.member.exception.SmsErrorCode;
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
    private final SmsCertificationRepository smsCertificationRepository;
    private final RedisTemplate<String, SmsCertification> redisTemplate;

    @Override
    public void saveSmsAuthToken(String phoneNumber, String value, SmsPrefix prefix) {
        smsCertificationRepository.save(SmsCertification.of(prefix.getTopic(phoneNumber), value));
    }

    @Override
    public boolean isCorrectCertificationNumber(String phoneNumber, String requestCertificationNumber, SmsPrefix prefix) {
        Optional<SmsCertification> smsCertification = smsCertificationRepository.findById(prefix.getTopic(phoneNumber));

        return smsCertification.map(certification -> certification.getCertificationNumber().equals(requestCertificationNumber)).orElse(false);
    }

    @Override
    public boolean existsCertificationNumber(String phoneNumber, SmsPrefix prefix) {
        return smsCertificationRepository.existsById(prefix.getTopic(phoneNumber));
    }

    @Override
    public void removeCertificationNumber(String phoneNumber, SmsPrefix prefix) {
        smsCertificationRepository.deleteById(prefix.getTopic(phoneNumber));
    }

    @Override
    public LocalDateTime getExpiredTime(String phoneNumber, SmsPrefix prefix) {
        Long ttl = redisTemplate.getExpire("smsCertification:" + prefix.getTopic(phoneNumber));
        log.info("ttl: {}", ttl);

        if (ttl == null || ttl < 0L)
            throw new GlobalErrorException(SmsErrorCode.EXPIRED_AUTH_CODE);

        return LocalDateTime.now().plusSeconds(ttl);
    }
}
