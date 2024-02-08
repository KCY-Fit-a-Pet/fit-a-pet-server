package kr.co.fitapet.infra.client.sms.snes.dto;

import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

public class SnesDto {
    /**
     * Client에서 SMS 인증 요청 시 사용되는 객체
     * @param to String : 수신번호
     */
    public record Request(
            String to
    ) {
        public static Request of(String to) {
            return new Request(to);
        }
    }

    @Builder
    public record SensReq(
            String type,
            String contentType,
            String countryCode,
            String from,
            String content,
            List<Request> messages
    ) {
        /**
         * NCP SMS API 요청 객체 생성
         * @param type String : SMS | LMS | MMS
         * @param contentType String : COMM | AD
         * @param countryCode String : 국가번호
         * @param from String : 발신번호
         * @param content String : 메시지 내용
         * @param messages List<SmsReq> : 메시지 정보 (to: 수신번호, subject: 개별 메시지 제목, content: 개별 메시지 내용)
         * @return SensReq : NCP SMS API 요청 객체
         */
        public static SensReq of(String type, String contentType, String countryCode, String from, String content, List<Request> messages) {
            return SensReq.builder()
                    .type(type)
                    .contentType(contentType)
                    .countryCode(countryCode)
                    .from(from)
                    .content(content)
                    .messages(messages)
                    .build();
        }
    }

    /**
     * Client에서 받은 SMS API 요청에 대한 응답 객체
     * @param requestId String : 요청 ID
     * @param requestTime LocalDateTime : 요청 시간
     * @param statusCode String : 응답 코드
     * @param statusName String : 응답 상태
     */
    @Builder
    public record SensRes(
            String requestId,
            LocalDateTime requestTime,
            String statusCode,
            String statusName
    ) {
    }

    /**
     * SMS 인증번호 발송 응답 객체
     * @param requestId String : 요청 ID (NCP SMS API 요청 시 발급된 요청 ID)
     * @param code String : 발급된 인증번호
     * @param requestTime LocalDateTime : 요청 시간
     * @param statusCode String : 응답 코드
     * @param statusName String : 응답 상태
     */
    @Builder
    public record SensInfo(
            String requestId,
            String code,
            LocalDateTime requestTime,
            String statusCode,
            String statusName
    ) {
        public static SensInfo from(SensRes sensRes, String code) {
            return SensInfo.builder()
                    .requestId(sensRes.requestId())
                    .code(code)
                    .requestTime(sensRes.requestTime())
                    .statusCode(sensRes.statusCode())
                    .statusName(sensRes.statusName())
                    .build();
        }
    }
}
