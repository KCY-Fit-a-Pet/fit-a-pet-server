package kr.co.fitapet.domain.common.redis.sms.provider;


import kr.co.fitapet.domain.common.redis.exception.RedisErrorException;
import kr.co.fitapet.domain.common.redis.sms.type.SmsPrefix;

import java.time.LocalDateTime;

public interface SmsRedisProvider {
    /**
     * SMS 인증 완료 후 계정 생성을 위한 토큰 저장
     * @param phone : String
     * @param code : String
     */
    void saveSmsAuthToken(String phone, String code, SmsPrefix prefix);

    /**
     * 인증번호 확인
     * @param phoneNumber : String
     * @param code : String
     * @param prefix : SmsPrefix
     * @return boolean : 인증번호 일치 여부
     */
    boolean isCorrectCode(String phoneNumber, String code, SmsPrefix prefix);

    /**
     * 휴대폰 번호에 해당하는 인증번호 존재 여부
     * @param phoneNumber : String
     * @param prefix : SmsPrefix
     * @return boolean : 인증번호 존재 여부
     */
    boolean isExistsCode(String phoneNumber, SmsPrefix prefix);

    /**
     * 인증번호 제거
     * @param phoneNumber : String
     * @param prefix : SmsPrefix
     */
    void removeCode(String phoneNumber, SmsPrefix prefix);

    /**
     * 인증번호 만료 시간 조회
     * @param phoneNumber : String
     * @param prefix : SmsPrefix
     * @return LocalDateTime : 인증번호 만료 시간
     * @throws RedisErrorException : RedisErrorCode.EXPIRED_VALUE(만료된 값)
     */
     LocalDateTime getExpiredTime(String phoneNumber, SmsPrefix prefix) throws RedisErrorException;

     default String getTopic(String phoneNumber, SmsPrefix prefix) {
         String str = prefix.getTopic(phoneNumber);
         return "sms" + str.substring(0, 1).toUpperCase()
                 + str.substring(1).replace("@", ":");
     }
}
