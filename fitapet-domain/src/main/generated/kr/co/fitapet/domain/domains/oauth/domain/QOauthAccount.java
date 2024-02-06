package kr.co.fitapet.domain.domains.oauth.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QOauthAccount is a Querydsl query type for OauthAccount
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QOauthAccount extends EntityPathBase<OauthAccount> {

    private static final long serialVersionUID = 1421209530L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QOauthAccount oauthAccount = new QOauthAccount("oauthAccount");

    public final kr.co.fitapet.domain.common.model.QDateAuditable _super = new kr.co.fitapet.domain.common.model.QDateAuditable(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final StringPath email = createString("email");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final kr.co.fitapet.domain.domains.member.domain.QMember member;

    public final StringPath oauthId = createString("oauthId");

    public final EnumPath<kr.co.fitapet.domain.domains.oauth.type.ProviderType> provider = createEnum("provider", kr.co.fitapet.domain.domains.oauth.type.ProviderType.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QOauthAccount(String variable) {
        this(OauthAccount.class, forVariable(variable), INITS);
    }

    public QOauthAccount(Path<? extends OauthAccount> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QOauthAccount(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QOauthAccount(PathMetadata metadata, PathInits inits) {
        this(OauthAccount.class, metadata, inits);
    }

    public QOauthAccount(Class<? extends OauthAccount> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.member = inits.isInitialized("member") ? new kr.co.fitapet.domain.domains.member.domain.QMember(forProperty("member"), inits.get("member")) : null;
    }

}

