package kr.co.fitapet.infra.client.fcm;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public enum NotificationDataKey {
    NOTICE_TYPE("noticeType"),
    FROM_ID("fromId"),
    FROM_NAME("fromName"),
    TO_ID("toId"),
    TO_NAME("toName"),
    DOMAIN_ID("domainId"),
    DOMAIN_NAME("domainName"),
    SUBJECT_ID("subjectId"),
    SUBJECT_NAME("subjectName"),
    ;
    private final String field;
    private static final List<NotificationDataKey> idFields = List.of(FROM_ID, TO_ID, DOMAIN_ID, SUBJECT_ID);
    private static final List<NotificationDataKey> nameFields = List.of(FROM_NAME, TO_NAME, DOMAIN_NAME, SUBJECT_NAME);

    @JsonValue
    public String getField() {
        return field;
    }

    public boolean isIdField() {
        return idFields.contains(this);
    }

    public boolean isNameField() {
        return nameFields.contains(this);
    }
}
