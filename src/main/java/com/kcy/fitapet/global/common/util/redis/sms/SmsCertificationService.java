package com.kcy.fitapet.global.common.util.redis.sms;

import java.time.LocalDateTime;

public interface SmsCertificationService {
    /**
     * SMS 인증 완료 후 계정 생성을 위한 토큰 저장
     * @param phoneNumber : String
     * @param key : String
     */
    void saveSmsAuthToken(String phoneNumber, String key, SmsPrefix prefix);

    /**
     * 인증번호 확인
     * @param phoneNumber : String
     * @param certificationNumber : String
     * @param prefix : SmsPrefix
     * @return boolean : 인증번호 일치 여부
     */
    boolean isCorrectCertificationNumber(String phoneNumber, String certificationNumber, SmsPrefix prefix);

    /**
     * 휴대폰 번호에 해당하는 인증번호 존재 여부
     * @param phoneNumber : String
     * @param prefix : SmsPrefix
     * @return boolean : 인증번호 존재 여부
     */
    boolean existsCertificationNumber(String phoneNumber, SmsPrefix prefix);

    /**
     * 인증번호 제거
     * @param phoneNumber : String
     * @param prefix : SmsPrefix
     */
    void removeCertificationNumber(String phoneNumber, SmsPrefix prefix);

    /**
     * 인증번호 만료 시간 조회
     * @param phoneNumber : String
     * @param prefix : SmsPrefix
     * @return LocalDateTime : 인증번호 만료 시간
     */
     LocalDateTime getExpiredTime(String phoneNumber, SmsPrefix prefix);
}
