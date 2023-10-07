package com.kcy.fitapet.global.common.util.redis.sms;

import java.time.LocalDateTime;

public interface SmsCertificationService {
    /**
     * 인증번호 발행
     * @param phoneNumber : String
     * @return String : 인증번호
     */
    String issueCertificationNumber(String phoneNumber);

    /**
     * SMS 인증 완료 후 계정 생성을 위한 토큰 저장
     * @param phoneNumber : String
     * @param accessToken : String
     */
    void saveSmsAuthToken(String phoneNumber, String accessToken);

    /**
     * 인증번호 확인
     * @param phoneNumber : String
     * @param certificationNumber : String
     * @return boolean : 인증번호 일치 여부
     */
    boolean isCorrectCertificationNumber(String phoneNumber, String certificationNumber);

    /**
     * 인증번호 제거
     * @param phoneNumber : String
     */
    void removeCertificationNumber(String phoneNumber);

    /**
     * 인증번호 만료 시간 조회
     * @param phoneNumber : String
     * @return LocalDateTime : 인증번호 만료 시간
     */
     LocalDateTime getExpiredTime(String phoneNumber);
}
