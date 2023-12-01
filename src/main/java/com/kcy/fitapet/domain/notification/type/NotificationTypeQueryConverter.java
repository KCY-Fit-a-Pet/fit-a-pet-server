package com.kcy.fitapet.domain.notification.type;

import org.springframework.core.convert.converter.Converter;

public class NotificationTypeQueryConverter implements Converter<String, NotificationType> {
    @Override
    public NotificationType convert(String source) {
        try {
            return NotificationType.valueOf(source.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw null;
        }
    }
}
