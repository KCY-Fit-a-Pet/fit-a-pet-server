package kr.co.fitapet.api.common.converter;

import kr.co.fitapet.domain.common.redis.sms.type.SmsPrefix;
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
