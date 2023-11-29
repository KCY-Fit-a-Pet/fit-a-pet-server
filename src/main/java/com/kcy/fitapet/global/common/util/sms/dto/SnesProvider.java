package com.kcy.fitapet.global.common.util.sms.dto;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kcy.fitapet.global.common.util.sms.SmsProvider;
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
    public SensRes sendCertificationNumber(SmsReq smsReq, String certificationNumber)
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
