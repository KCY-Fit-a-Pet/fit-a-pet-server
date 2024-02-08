package kr.co.fitapet.domain.domains.member.domain;

import jakarta.persistence.*;
import kr.co.fitapet.domain.common.model.DateAuditable;
import kr.co.fitapet.domain.domains.member.type.RoleType;
import kr.co.fitapet.domain.common.converter.RoleTypeConverter;
import kr.co.fitapet.domain.domains.notification.domain.Notification;
import kr.co.fitapet.domain.domains.notification.type.NotificationType;
import kr.co.fitapet.domain.domains.oauth.domain.OauthAccount;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Table(name = "MEMBER")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DynamicInsert
@ToString(of = {"id", "name", "phone", "email", "role"})
public class Member extends DateAuditable {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String uid;
    private String name;
    private String password;
    private String phone;
    private String email;
    @Column(name = "profile_img") @ColumnDefault("NULL") @Getter
    private String profileImg;
    @Column(name = "account_locked") @ColumnDefault("false")
    private Boolean accountLocked;
    @Column(name = "is_oauth") @ColumnDefault("false") @Getter
    private Boolean isOauth;
    @Convert(converter = RoleTypeConverter.class)
    private RoleType role;
    @Embedded
    private NotificationSetting notificationSetting;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private Set<OauthAccount> oauthIDs = new HashSet<>();
    @OneToMany(mappedBy = "from", cascade = CascadeType.ALL)
    private Set<MemberNickname> fromMemberNickname = new HashSet<>();
    @OneToMany(mappedBy = "to", cascade = CascadeType.ALL)
    private Set<MemberNickname> toMemberNickname = new HashSet<>();
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<Notification> notifications = new ArrayList<>();
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL) @Getter
    private List<Manager> underCares = new ArrayList<>();
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL) @Getter
    private List<OauthAccount> oauthAccounts = new ArrayList<>();

    @Builder
    private Member(String uid, String name, String password, String phone,
                   String email, String profileImg, Boolean accountLocked, Boolean isOauth,
                   RoleType role, NotificationSetting notificationSetting) {
        this.uid = uid;
        this.name = name;
        this.password = password;
        this.phone = phone;
        this.email = email;
        this.profileImg = profileImg;
        this.accountLocked = accountLocked;
        this.isOauth = isOauth;
        this.role = role;
        this.notificationSetting = notificationSetting;
    }

    public static Member of(String uid, String name, String password, String phone, String email,
                            String profileImg, Boolean accountLocked, Boolean isOauth,
                            RoleType role, NotificationSetting notificationSetting) {
        return Member.builder()
                .uid(uid)
                .name(name)
                .password(password)
                .phone(phone)
                .email(email)
                .profileImg(profileImg)
                .accountLocked(accountLocked)
                .isOauth(isOauth)
                .role(role)
                .notificationSetting(notificationSetting)
                .build();
    }

    /**
     * 비밀번호 변경
     * @param encodedPassword : 변경할 비밀번호는 암호화되어 있어야 함
     */
    public void updateEncodedPassword(String encodedPassword) {
        this.password = encodedPassword;
    }

    public void updateName(String name) {
        this.name = name;
    }

    public void updateNotificationFromType(NotificationType type) {
        this.notificationSetting.updateNotificationFromType(type);
    }

    public void updateOathToOriginAccount() {
        this.isOauth = Boolean.FALSE;
    }
}
