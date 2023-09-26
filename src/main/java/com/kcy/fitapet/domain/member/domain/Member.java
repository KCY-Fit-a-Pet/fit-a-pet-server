package com.kcy.fitapet.domain.member.domain;

import com.kcy.fitapet.domain.model.Auditable;
import com.kcy.fitapet.domain.notification.domain.Notification;
import com.kcy.fitapet.domain.notification.domain.NotificationSetting;
import com.kcy.fitapet.domain.oauthid.domain.OAuthID;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

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
    private Long id;

    private String name;
    private String password;
    private String phone;
    private String email;
    private String profile_img;
    @ColumnDefault("false")
    private boolean account_locked;
    @Convert(converter = RoleTypeConverter.class)
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
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<Master> pets = new ArrayList<>();

}
