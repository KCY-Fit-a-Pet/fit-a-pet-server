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

    @JsonValue
    public String getField() {
        return field;
    }

    public List<String> getNameFields() {
        return List.of("fromName", "toName", "domainName", "subjectName");
    }
}
