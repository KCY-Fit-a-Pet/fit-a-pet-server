package com.kcy.fitapet.global.common.util.redis.sms;

import com.kcy.fitapet.domain.member.exception.SmsErrorCode;
import com.kcy.fitapet.global.common.response.exception.GlobalErrorException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.concurrent.ThreadLocalRandom;

@Slf4j
@Service
@RequiredArgsConstructor
public class SmsCertificationServiceImpl implements SmsCertificationService {
    private final SmsCertificationRepository smsCertificationRepository;
    private final RedisTemplate<String, SmsCertification> redisTemplate;

    @Override
    public void saveSmsAuthToken(String phoneNumber, String value) {
        smsCertificationRepository.save(SmsCertification.of(phoneNumber, value));
    }

    @Override
    public boolean isCorrectCertificationNumber(String phoneNumber, String requestCertificationNumber) {
        SmsCertification smsCertification = smsCertificationRepository.findById(phoneNumber)
                .orElseThrow(() -> new GlobalErrorException(SmsErrorCode.EXPIRED_AUTH_CODE));

        return smsCertification.getCertificationNumber().equals(requestCertificationNumber);
    }

    @Override
    public void removeCertificationNumber(String phoneNumber) {
        smsCertificationRepository.deleteById(phoneNumber);
    }

    @Override
    public LocalDateTime getExpiredTime(String phoneNumber) {
        Long ttl = redisTemplate.getExpire("smsCertification:" + phoneNumber);
        log.info("ttl: {}", ttl);

        if (ttl == null || ttl < 0L)
            throw new GlobalErrorException(SmsErrorCode.EXPIRED_AUTH_CODE);

        return LocalDateTime.now().plusSeconds(ttl);
    }
}
