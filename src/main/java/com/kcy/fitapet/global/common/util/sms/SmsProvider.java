package com.kcy.fitapet.global.common.util.sms;

import com.kcy.fitapet.global.common.response.exception.GlobalErrorException;
import com.kcy.fitapet.global.common.util.sms.dto.SensInfo;
import com.kcy.fitapet.global.common.util.sms.dto.SmsReq;

public interface SmsProvider {
    /**
     * 인증번호를 수신자에게 SMS로 전송
     * @param dto : 수신자 번호
     * @return SensInfo : SMS 전송 정보
     */
    SensInfo sendCodeByPhoneNumber(SmsReq dto) throws GlobalErrorException;
}
