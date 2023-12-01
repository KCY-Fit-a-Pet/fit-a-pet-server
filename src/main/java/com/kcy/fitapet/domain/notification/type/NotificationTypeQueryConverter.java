package com.kcy.fitapet.domain.notification.type;

import com.kcy.fitapet.global.common.response.code.ErrorCode;
import com.kcy.fitapet.global.common.response.exception.GlobalErrorException;
import org.springframework.core.convert.converter.Converter;

public class NotificationTypeQueryConverter implements Converter<String, NotificationType> {
    @Override
    public NotificationType convert(String source) {
        try {
            return NotificationType.valueOf(source.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw  new GlobalErrorException(ErrorCode.INVALID_QUERY_TYPE);
        }
    }
}
