package com.kcy.fitapet.domain.member.domain;

import com.kcy.fitapet.domain.model.Auditable;
import com.kcy.fitapet.domain.notification.domain.Notification;
import com.kcy.fitapet.domain.notification.domain.NotificationSetting;
import com.kcy.fitapet.domain.oauthid.domain.OAuthID;
import com.kcy.fitapet.domain.pet.domain.Pet;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "MEMBER")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(of = {"id", "name", "phone", "email", "role"})
public class Member extends Auditable {
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
    @Column(name = "profile_img") @Getter
    private String profileImg;
    @Column(name = "account_locked") @ColumnDefault("false")
    private Boolean accountLocked;
    @Convert(converter = RoleTypeConverter.class)
    @Getter
    private RoleType role;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private Set<OAuthID> oauthIDs = new HashSet<>();
    @OneToMany(mappedBy = "from", cascade = CascadeType.ALL)
    private Set<MemberNickname> fromMemberNickname = new HashSet<>();
    @OneToMany(mappedBy = "to", cascade = CascadeType.ALL)
    private Set<MemberNickname> toMemberNickname = new HashSet<>();
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<Notification> notifications = new ArrayList<>();
    @OneToOne(mappedBy = "member", cascade = CascadeType.ALL)
    private NotificationSetting notificationSetting;
    @OneToMany(mappedBy = "master") @Getter
    private List<Pet> pets = new ArrayList<>();
    @OneToMany(mappedBy = "manager", cascade = CascadeType.ALL) @Getter
    private List<Manager> underCares = new ArrayList<>();

    @Builder
    private Member(String uid, String name, String password, String phone, String email, String profileImg, Boolean accountLocked, RoleType role) {
        this.uid = uid;
        this.name = name;
        this.password = password;
        this.phone = phone;
        this.email = email;
        this.profileImg = profileImg;
        this.accountLocked = accountLocked;
        this.role = role;
    }

    public static Member of(String uid, String name, String password, String phone, String email, String profileImg, Boolean accountLocked, RoleType role) {
        return Member.builder()
                .uid(uid)
                .name(name)
                .password(password)
                .phone(phone)
                .email(email)
                .profileImg(profileImg)
                .accountLocked(accountLocked)
                .role(role)
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
     * @return true or false
     */
    public boolean checkPassword(String plainPassword, PasswordEncoder passwordEncoder) {
        return passwordEncoder.matches(passwordEncoder.encode(plainPassword), this.password);
    }
}
