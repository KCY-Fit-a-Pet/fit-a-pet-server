package kr.co.fitapet.domain.domains.member.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QMember is a Querydsl query type for Member
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QMember extends EntityPathBase<Member> {

    private static final long serialVersionUID = 1944946495L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QMember member = new QMember("member1");

    public final kr.co.fitapet.domain.common.model.QDateAuditable _super = new kr.co.fitapet.domain.common.model.QDateAuditable(this);

    public final BooleanPath accountLocked = createBoolean("accountLocked");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final StringPath email = createString("email");

    public final SetPath<MemberNickname, QMemberNickname> fromMemberNickname = this.<MemberNickname, QMemberNickname>createSet("fromMemberNickname", MemberNickname.class, QMemberNickname.class, PathInits.DIRECT2);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final BooleanPath isOauth = createBoolean("isOauth");

    public final StringPath name = createString("name");

    public final ListPath<kr.co.fitapet.domain.domains.notification.domain.Notification, kr.co.fitapet.domain.domains.notification.domain.QNotification> notifications = this.<kr.co.fitapet.domain.domains.notification.domain.Notification, kr.co.fitapet.domain.domains.notification.domain.QNotification>createList("notifications", kr.co.fitapet.domain.domains.notification.domain.Notification.class, kr.co.fitapet.domain.domains.notification.domain.QNotification.class, PathInits.DIRECT2);

    public final QNotificationSetting notificationSetting;

    public final ListPath<kr.co.fitapet.domain.domains.oauth.domain.OauthAccount, kr.co.fitapet.domain.domains.oauth.domain.QOauthAccount> oauthAccounts = this.<kr.co.fitapet.domain.domains.oauth.domain.OauthAccount, kr.co.fitapet.domain.domains.oauth.domain.QOauthAccount>createList("oauthAccounts", kr.co.fitapet.domain.domains.oauth.domain.OauthAccount.class, kr.co.fitapet.domain.domains.oauth.domain.QOauthAccount.class, PathInits.DIRECT2);

    public final SetPath<kr.co.fitapet.domain.domains.oauth.domain.OauthAccount, kr.co.fitapet.domain.domains.oauth.domain.QOauthAccount> oauthIDs = this.<kr.co.fitapet.domain.domains.oauth.domain.OauthAccount, kr.co.fitapet.domain.domains.oauth.domain.QOauthAccount>createSet("oauthIDs", kr.co.fitapet.domain.domains.oauth.domain.OauthAccount.class, kr.co.fitapet.domain.domains.oauth.domain.QOauthAccount.class, PathInits.DIRECT2);

    public final StringPath password = createString("password");

    public final StringPath phone = createString("phone");

    public final StringPath profileImg = createString("profileImg");

    public final EnumPath<kr.co.fitapet.domain.domains.member.type.RoleType> role = createEnum("role", kr.co.fitapet.domain.domains.member.type.RoleType.class);

    public final SetPath<MemberNickname, QMemberNickname> toMemberNickname = this.<MemberNickname, QMemberNickname>createSet("toMemberNickname", MemberNickname.class, QMemberNickname.class, PathInits.DIRECT2);

    public final StringPath uid = createString("uid");

    public final ListPath<Manager, QManager> underCares = this.<Manager, QManager>createList("underCares", Manager.class, QManager.class, PathInits.DIRECT2);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QMember(String variable) {
        this(Member.class, forVariable(variable), INITS);
    }

    public QMember(Path<? extends Member> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QMember(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QMember(PathMetadata metadata, PathInits inits) {
        this(Member.class, metadata, inits);
    }

    public QMember(Class<? extends Member> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.notificationSetting = inits.isInitialized("notificationSetting") ? new QNotificationSetting(forProperty("notificationSetting")) : null;
    }

}

