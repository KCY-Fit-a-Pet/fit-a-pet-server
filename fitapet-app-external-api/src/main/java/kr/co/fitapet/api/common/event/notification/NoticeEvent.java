package kr.co.fitapet.api.common.event.notification;

import kr.co.fitapet.domain.domains.notification.domain.Notification;
import kr.co.fitapet.domain.domains.notification.type.NotificationType;
import kr.co.fitapet.infra.client.fcm.NotificationDataKey;
import lombok.Builder;

import java.util.Map;
import java.util.stream.Collectors;

/**
 * FCM Push Notification Event를 위한 Data Type Class
 *
 * @param memberId Long : 푸시 알림을 받을 회원 ID
 * @param noticeType {@link NoticeType} : push 알림을 위한 알림 타입
 * @param notificationType {@link NotificationType} : DB에 저장될 알림 타입
 * @param title String : 푸시 알림 제목
 * @param content String : 푸시 알림 내용. {@link NoticeType}에 따라 동적으로 생성
 * @param imageUrl String : 푸시 알림 이미지 URL (optional)
 * @param data Map<{@link NotificationDataKey}, String> : 푸시 알림에 포함될 데이터
 */
@Builder
public record NoticeEvent(
        Long memberId,
        NoticeType noticeType,
        NotificationType notificationType,
        String title,
        String content,
        String imageUrl,
        Map<NotificationDataKey, String> data
) {
    public Notification toEntity() {
        Map<String, Long> idFields = getIdFields();

        return Notification.builder()
                .title(title)
                .content(getFormattedContent())
                .imageUrl(imageUrl)
                .ctype(notificationType)
                .fromId(idFields.getOrDefault(NotificationDataKey.FROM_ID.getField(), null))
                .toId(idFields.getOrDefault(NotificationDataKey.TO_ID.getField(), null))
                .domainId(idFields.getOrDefault(NotificationDataKey.DOMAIN_ID.getField(), null))
                .subjectId(idFields.getOrDefault(NotificationDataKey.SUBJECT_ID.getField(), null))
                .build();
    }

    /**
     * data의 정보를 {@link NoticeType}에 따라 동적으로 생성된 content를 반환합니다.
     * @return String : 동적으로 생성된 content
     */
    public String getFormattedContent() {
        return noticeType.createFormattedContent(getNameFields());
    }

    private Map<String, Long> getIdFields() {
        return data.entrySet().stream()
                .filter(e -> e.getKey().isIdField())
                .collect(
                        Collectors.toMap(
                                e -> e.getKey().getField(),
                                e -> Long.parseLong(e.getValue())
                        )
                );
    }

    private Map<String, String> getNameFields() {
        return data.entrySet().stream()
                .filter(e -> e.getKey().isNameField())
                .collect(
                        Collectors.toMap(
                                e -> e.getKey().getField(),
                                Map.Entry::getValue
                        )
                );
    }
}
