package kr.co.fitapet.api.common.converter;


import kr.co.fitapet.domain.domains.notification.type.NotificationType;
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
