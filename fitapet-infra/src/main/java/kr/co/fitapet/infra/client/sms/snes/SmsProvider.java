package kr.co.fitapet.infra.client.sms.snes;

import kr.co.fitapet.common.execption.GlobalErrorException;
import kr.co.fitapet.infra.client.sms.snes.dto.SnesDto;

public interface SmsProvider {
    /**
     * 인증번호를 수신자에게 SMS로 전송
     * @param dto : 수신자 번호
     * @return SensInfo : SMS 전송 정보
     */
    SnesDto.SensInfo sendCodeByPhoneNumber(SnesDto.Request dto) throws GlobalErrorException;
}
