package com.kcy.fitapet.domain.member.domain;

import com.kcy.fitapet.domain.care.domain.CareCategory;
import com.kcy.fitapet.domain.member.type.RoleType;
import com.kcy.fitapet.domain.member.type.converter.RoleTypeConverter;
import com.kcy.fitapet.domain.model.DateAuditable;
import com.kcy.fitapet.domain.notification.domain.Notification;
import com.kcy.fitapet.domain.notification.type.NotificationType;
import com.kcy.fitapet.domain.oauth.domain.OauthAccount;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.springframework.aot.hint.MemberCategory;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "MEMBER")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DynamicInsert
@ToString(of = {"id", "name", "phone", "email", "role"})
public class Member extends DateAuditable {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    private Long id;

    @Getter
    private String uid;
    @Getter
    private String name;
    private String password;
    @Getter
    private String phone;
    @Getter
    private String email;
    @Column(name = "profile_img") @ColumnDefault("NULL") @Getter
    private String profileImg;
    @Column(name = "account_locked") @ColumnDefault("false")
    private Boolean accountLocked;
    @Column(name = "is_oauth") @ColumnDefault("false") @Getter
    private Boolean isOauth;
    @Convert(converter = RoleTypeConverter.class)
    @Getter
    private RoleType role;
    @Embedded @Getter
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
     * 비밀번호 암호화
     * @param passwordEncoder : 비밀번호 암호화 객체
     * @return 변경된 유저 객체
     */
    public void encodePassword(PasswordEncoder passwordEncoder) {
        this.password = passwordEncoder.encode(this.password);
    }

    /**
     * 비밀번호 확인
     * @param plainPassword : 평문 비밀번호
     * @param passwordEncoder : 비밀번호 암호화 객체
     * @return true or false : 비밀번호가 일치하면 true, 일치하지 않으면 false
     */
    public boolean checkPassword(String plainPassword, PasswordEncoder passwordEncoder) {
        return passwordEncoder.matches(plainPassword, this.password);
    }

    public void updateName(String name) {
        this.name = name;
    }

    public void updatePassword(String password, PasswordEncoder passwordEncoder) {
        this.password = passwordEncoder.encode(password);
    }

    public void updateNotificationFromType(NotificationType type) {
        this.notificationSetting.updateNotificationFromType(type);
    }

    public void updateOathToOriginAccount() {
        this.isOauth = Boolean.FALSE;
    }
}
