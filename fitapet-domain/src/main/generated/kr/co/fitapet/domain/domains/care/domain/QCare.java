package kr.co.fitapet.domain.domains.care.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QCare is a Querydsl query type for Care
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QCare extends EntityPathBase<Care> {

    private static final long serialVersionUID = 949457581L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QCare care = new QCare("care");

    public final kr.co.fitapet.domain.common.model.QAuthorAuditable _super;

    // inherited
    public final kr.co.fitapet.domain.domains.member.domain.QMember author;

    public final QCareCategory careCategory;

    public final ListPath<CareDate, QCareDate> careDates = this.<CareDate, QCareDate>createList("careDates", CareDate.class, QCareDate.class, PathInits.DIRECT2);

    public final StringPath careName = createString("careName");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    // inherited
    public final kr.co.fitapet.domain.domains.member.domain.QMember lastEditorId;

    public final NumberPath<Integer> limitTime = createNumber("limitTime", Integer.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt;

    public QCare(String variable) {
        this(Care.class, forVariable(variable), INITS);
    }

    public QCare(Path<? extends Care> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QCare(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QCare(PathMetadata metadata, PathInits inits) {
        this(Care.class, metadata, inits);
    }

    public QCare(Class<? extends Care> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this._super = new kr.co.fitapet.domain.common.model.QAuthorAuditable(type, metadata, inits);
        this.author = _super.author;
        this.careCategory = inits.isInitialized("careCategory") ? new QCareCategory(forProperty("careCategory"), inits.get("careCategory")) : null;
        this.createdAt = _super.createdAt;
        this.lastEditorId = _super.lastEditorId;
        this.updatedAt = _super.updatedAt;
    }

}

