package com.kcy.fitapet.global.common.util.sms.snes;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kcy.fitapet.domain.member.exception.SmsErrorCode;
import com.kcy.fitapet.global.common.response.code.ErrorCode;
import com.kcy.fitapet.global.common.response.exception.GlobalErrorException;
import com.kcy.fitapet.global.common.util.sms.SmsProvider;
import com.kcy.fitapet.global.common.util.sms.dto.SensInfo;
import com.kcy.fitapet.global.common.util.sms.dto.SensReq;
import com.kcy.fitapet.global.common.util.sms.dto.SensRes;
import com.kcy.fitapet.global.common.util.sms.dto.SmsReq;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Slf4j
@Component
public class SnesProvider implements SmsProvider {
    private final ObjectMapper objectMapper;

    private final String accessKey;
    private final String secretKey;
    private final String serviceId;
    private final String phone;

    public SnesProvider(
            @Value("${ncp.api-key}") String accessKey,
            @Value("${ncp.secret-key}") String secretKey,
            @Value("${ncp.sms.service-key}") String serviceId,
            @Value("${ncp.sms.sender-phone}") String phone,
            @Autowired ObjectMapper objectMapper
    ) {
        this.accessKey = accessKey;
        this.secretKey = secretKey;
        this.serviceId = serviceId;
        this.phone = phone;
        this.objectMapper = objectMapper;
    }

    @Override
    public SensInfo sendCodeByPhoneNumber(SmsReq dto) throws GlobalErrorException {
        String certificationNumber = issueCertificationNumber(dto.to());

        SensRes sensRes;
        try {
            sensRes = sendCertificationNumber(dto, certificationNumber);
        } catch (Exception e) {
            log.warn("SMS 발송 실패: {}", e.getMessage());
            throw new GlobalErrorException(ErrorCode.SMS_SEND_ERROR);
        }
        checkSmsStatus(dto.to(), sensRes);

        return SensInfo.from(sensRes, certificationNumber);
    }

    private SensRes sendCertificationNumber(SmsReq smsReq, String certificationNumber)
            throws JsonProcessingException, RestClientException, InvalidKeyException, NoSuchAlgorithmException, UnsupportedEncodingException {
        long now = System.currentTimeMillis();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("x-ncp-apigw-timestamp", String.valueOf(now));
        headers.set("x-ncp-iam-access-key", accessKey);
        headers.set("x-ncp-apigw-signature-v2", makeSignature(now));

        List<SmsReq> messages = List.of(smsReq);

        SensReq request = SensReq.of("SMS", "COMM", "82", phone, createAuthCodeMessage(certificationNumber), messages);

        String body = objectMapper.writeValueAsString(request);
        HttpEntity<String> httpEntity = new HttpEntity<>(body, headers);

        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory());

        return restTemplate.postForObject("https://sens.apigw.ntruss.com/sms/v2/services/" + serviceId + "/messages", httpEntity, SensRes.class);
    }

    private String issueCertificationNumber(String phoneNumber) {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < 6; i++) {
            sb.append(ThreadLocalRandom.current().nextInt(0, 10));
        }
        String code = sb.toString();

        return code;
    }

    private void checkSmsStatus(String phoneNumber, SensRes sensRes) {
        if (sensRes.statusCode().equals("404")) {
            log.warn("존재하지 않는 수신자: {}", phoneNumber);
            throw new GlobalErrorException(SmsErrorCode.INVALID_RECEIVER);
        } else if (sensRes.statusName().equals("fail")) {
            log.warn("SMS API 응답 실패: {}", sensRes);
            throw new GlobalErrorException(ErrorCode.SMS_SEND_ERROR);
        }
        log.info("SMS 발송 성공");
    }

    private String makeSignature(long now) throws NoSuchAlgorithmException, UnsupportedEncodingException, InvalidKeyException {
        String space = " ";
        String newLine = "\n";
        String method = "POST";
        String url = "/sms/v2/services/" + this.serviceId + "/messages";	// url (include query string)
        String timestamp = String.valueOf(now);
        String accessKey = this.accessKey;
        String secretKey = this.secretKey;

        String message = new StringBuilder()
                .append(method)
                .append(space)
                .append(url)
                .append(newLine)
                .append(timestamp)
                .append(newLine)
                .append(accessKey)
                .toString();

        SecretKeySpec signingKey = new SecretKeySpec(secretKey.getBytes("UTF-8"), "HmacSHA256");
        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(signingKey);

        byte[] rawHmac = mac.doFinal(message.getBytes("UTF-8"));

        return Base64.encodeBase64String(rawHmac);
    }

    private String createAuthCodeMessage(String code) {
        return "[Fit a Pet] 인증번호 [" + code + "]를 입력해주세요.";
    }
}
