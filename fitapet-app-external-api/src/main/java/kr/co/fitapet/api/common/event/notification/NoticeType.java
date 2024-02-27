package kr.co.fitapet.api.common.event.notification;

import lombok.Getter;

import java.text.MessageFormat;
import java.util.List;
import java.util.Map;

/**
 * 알림 타입을 정의하는 클래스입니다.
 *
 * <p>
 * 알림 타입은 [DOMAIN]_[ACTION]_[FROM]_[TO] 혹은 [DOMAIN]_[ACTION]_[FROM]_[SUBJECT] 형식으로 정의합니다. (FROM, TO, SUBJECT는 선택사항입니다.)
 * NoticeContentType은 각 알림 타입에 대한 이름, 제목 및 내용을 정의합니다.
 * </p>
 */
@Getter
public enum NoticeType {
    /* 공지사항 */
    ANNOUNCEMENT("%s", "%s"),

    /* 케어 */
    CARE_DO_FROM_SUBJECT("케어 완료", "%s님이 %s의 %s 케어를 완료했어요.", "fromName", "subjectName", "domainName"), // from, 반려동물 이름(SUBJECT), 케어이름(DOMAIN)
    CARE_CANCEL_FROM_SUBJECT("케어 취소", "%s님이 %s의 %s 케어를 취소했어요.", "fromName", "subjectName", "domainName"), // from, 반려동물 이름(SUBJECT), 케어이름(DOMAIN)
    CARE_ALARM_SUBJECT("케어 알림", "%s의 %s 케어가 아직 완료되지 않았어요.", "subjectName", "domainName"), // 반려동물 이름(SUBJECT), 케어이름(DOMAIN)

    /* 멤버 */
    MANAGER_INVITED_FROM_SUBJECT("관리자 초대", "%s님이 %s 관리에 초대했어요.", "fromName", "subjectName"), // from, 반려동물 이름(SUBJECT)
    MANAGER_ACCEPT_FROM_SUBJECT("관리자 초대 승인", "%s님이 %s의 케어 멤버 초대를 수락했어요.", "fromName", "subjectName"), // from, 반려동물 이름(SUBJECT)
    MANAGER_JOIN_FROM_SUBJECT("관리자 가입", "%s님이 %s의 케어 멤버로 참여했어요.", "fromName", "subjectName"), // from, 반려동물 이름(SUBJECT)
    MANAGER_DELEGATE_TO_SUBJECT("관리자 위임", "%s의 관리자가 %s님으로 변경되었어요.", "subjectName", "toName"), // 반려동물 이름(SUBJECT), to
    ;

    private final String title;
    private final String contentFormat;
    private final List<String> order;

    NoticeType(String title, String contentFormat, String ...args) {
        this.title = title;
        this.contentFormat = contentFormat;
        this.order = List.of(args);
    }

    /**
     * 알림 타입에 맞는 내용을 생성합니다.
     * @param args Map<String, String> : 알림 타입에 맞는 인자
     * @return String : content
     */
    public String createFormattedContent(Map<String, String> args) {
        if (this.name().equals(ANNOUNCEMENT.name())) throw new IllegalStateException("공지사항은 포맷팅할 수 없습니다.");
        if (args.size() != order.size()) throw new IllegalArgumentException("포맷팅할 수 없는 인자입니다.");

        Object[] arguments = order.stream().map(args::get).toArray();
        return MessageFormat.format(contentFormat, arguments);
    }
}
