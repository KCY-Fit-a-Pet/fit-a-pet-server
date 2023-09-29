package com.kcy.fitapet.global.common.util.redis.sms;

import com.kcy.fitapet.global.common.response.code.ErrorCode;
import com.kcy.fitapet.global.common.response.exception.GlobalErrorException;
import com.kcy.fitapet.global.common.util.jwt.JwtUtil;
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

    private final JwtUtil jwtUtil;

    @Override
    public String issueCertificationNumber(String phoneNumber) {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < 6; i++) {
            sb.append(ThreadLocalRandom.current().nextInt(0, 10));
        }
        String code = sb.toString();

        SmsCertification smsCertification = SmsCertification.of(phoneNumber, code);
        smsCertificationRepository.save(smsCertification);

        return code;
    }

    @Override
    public String issueSmsAuthToken(String phoneNumber) {
        String token = jwtUtil.generateSmsAuthToken(phoneNumber);
        smsCertificationRepository.save(SmsCertification.of(phoneNumber, token));
        return token;
    }

    @Override
    public boolean isCorrectCertificationNumber(String phoneNumber, String requestCertificationNumber) {
        SmsCertification smsCertification = smsCertificationRepository.findById(phoneNumber)
                .orElseThrow(() -> new GlobalErrorException(ErrorCode.EXPIRED_AUTH_CODE));

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

        if (ttl == null || ttl < 0) {
            throw new GlobalErrorException(ErrorCode.EXPIRED_AUTH_CODE);
        }
        return LocalDateTime.now().plusSeconds(ttl);
    }
}
