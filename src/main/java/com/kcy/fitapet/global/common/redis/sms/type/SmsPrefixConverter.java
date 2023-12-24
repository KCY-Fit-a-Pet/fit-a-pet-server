package com.kcy.fitapet.global.common.redis.sms.type;

import com.kcy.fitapet.global.common.response.code.ErrorCode;
import com.kcy.fitapet.global.common.response.exception.GlobalErrorException;
import org.springframework.core.convert.converter.Converter;

public class SmsPrefixConverter implements Converter<String, SmsPrefix> {
    @Override
    public SmsPrefix convert(String source) {
        try {
            SmsPrefix prefix = SmsPrefix.valueOf(source.toUpperCase());
            if (prefix.equals(SmsPrefix.REGISTER))
                throw new GlobalErrorException(ErrorCode.INVALID_QUERY_TYPE);
            return prefix;
        } catch (IllegalArgumentException e) {
            throw new GlobalErrorException(ErrorCode.INVALID_QUERY_TYPE);
        }
    }
}
