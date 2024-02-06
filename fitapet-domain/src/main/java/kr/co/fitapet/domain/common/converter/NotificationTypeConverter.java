package kr.co.fitapet.domain.common.converter;

import jakarta.persistence.Converter;
import kr.co.fitapet.domain.common.util.converter.AbstractLegacyEnumAttributeConverter;
import kr.co.fitapet.domain.domains.notification.type.NotificationType;

@Converter
public class NotificationTypeConverter extends AbstractLegacyEnumAttributeConverter<NotificationType> {
    private static final String ENUM_NAME = "알림타입";

    public NotificationTypeConverter() {
        super(NotificationType.class, false, ENUM_NAME);
    }
}
