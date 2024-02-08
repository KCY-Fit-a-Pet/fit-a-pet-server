package kr.co.fitapet.domain.domains.member.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QMemberNickname is a Querydsl query type for MemberNickname
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QMemberNickname extends EntityPathBase<MemberNickname> {

    private static final long serialVersionUID = -765532979L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QMemberNickname memberNickname = new QMemberNickname("memberNickname");

    public final kr.co.fitapet.domain.common.model.QDateAuditable _super = new kr.co.fitapet.domain.common.model.QDateAuditable(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final QMember from;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath nickname = createString("nickname");

    public final QMember to;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QMemberNickname(String variable) {
        this(MemberNickname.class, forVariable(variable), INITS);
    }

    public QMemberNickname(Path<? extends MemberNickname> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QMemberNickname(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QMemberNickname(PathMetadata metadata, PathInits inits) {
        this(MemberNickname.class, metadata, inits);
    }

    public QMemberNickname(Class<? extends MemberNickname> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.from = inits.isInitialized("from") ? new QMember(forProperty("from"), inits.get("from")) : null;
        this.to = inits.isInitialized("to") ? new QMember(forProperty("to"), inits.get("to")) : null;
    }

}

