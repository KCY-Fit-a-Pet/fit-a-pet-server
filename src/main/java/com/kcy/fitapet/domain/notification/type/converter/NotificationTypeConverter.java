package com.kcy.fitapet.domain.notification.type.converter;

import com.kcy.fitapet.domain.notification.type.NotificationType;
import com.kcy.fitapet.global.common.util.converter.AbstractLegacyEnumAttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class NotificationTypeConverter extends AbstractLegacyEnumAttributeConverter<NotificationType> {
    private static final String ENUM_NAME = "알림타입";

    public NotificationTypeConverter() {
        super(NotificationType.class, false, ENUM_NAME);
    }
}
