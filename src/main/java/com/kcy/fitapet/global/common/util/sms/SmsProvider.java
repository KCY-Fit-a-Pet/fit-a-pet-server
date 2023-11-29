package com.kcy.fitapet.global.common.util.sms;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.kcy.fitapet.global.common.util.sms.dto.SensRes;
import com.kcy.fitapet.global.common.util.sms.dto.SmsReq;
import org.springframework.web.client.RestClientException;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public interface SmsProvider {
    /**
     * 인증번호를 수신자에게 SMS로 전송
     * @param smsReq : 수신자 번호
     * @param certificationNumber : 인증번호
     * @return SensRes : SMS API 요청에 대한 응답 객체
     */
    SensRes sendCertificationNumber(SmsReq smsReq, String certificationNumber) throws JsonProcessingException, RestClientException, InvalidKeyException, NoSuchAlgorithmException, UnsupportedEncodingException;
}
