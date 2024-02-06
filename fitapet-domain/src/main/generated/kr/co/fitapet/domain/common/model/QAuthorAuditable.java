package kr.co.fitapet.domain.common.model;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QAuthorAuditable is a Querydsl query type for AuthorAuditable
 */
@Generated("com.querydsl.codegen.DefaultSupertypeSerializer")
public class QAuthorAuditable extends EntityPathBase<AuthorAuditable> {

    private static final long serialVersionUID = -1904726574L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QAuthorAuditable authorAuditable = new QAuthorAuditable("authorAuditable");

    public final kr.co.fitapet.domain.domains.member.domain.QMember author;

    public final DateTimePath<java.time.LocalDateTime> createdAt = createDateTime("createdAt", java.time.LocalDateTime.class);

    public final kr.co.fitapet.domain.domains.member.domain.QMember lastEditorId;

    public final DateTimePath<java.time.LocalDateTime> updatedAt = createDateTime("updatedAt", java.time.LocalDateTime.class);

    public QAuthorAuditable(String variable) {
        this(AuthorAuditable.class, forVariable(variable), INITS);
    }

    public QAuthorAuditable(Path<? extends AuthorAuditable> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QAuthorAuditable(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QAuthorAuditable(PathMetadata metadata, PathInits inits) {
        this(AuthorAuditable.class, metadata, inits);
    }

    public QAuthorAuditable(Class<? extends AuthorAuditable> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.author = inits.isInitialized("author") ? new kr.co.fitapet.domain.domains.member.domain.QMember(forProperty("author"), inits.get("author")) : null;
        this.lastEditorId = inits.isInitialized("lastEditorId") ? new kr.co.fitapet.domain.domains.member.domain.QMember(forProperty("lastEditorId"), inits.get("lastEditorId")) : null;
    }

}

