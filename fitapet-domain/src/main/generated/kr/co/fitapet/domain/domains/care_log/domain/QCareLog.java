package kr.co.fitapet.domain.domains.care_log.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QCareLog is a Querydsl query type for CareLog
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QCareLog extends EntityPathBase<CareLog> {

    private static final long serialVersionUID = -1860243406L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QCareLog careLog = new QCareLog("careLog");

    public final kr.co.fitapet.domain.domains.member.domain.QMember author;

    public final kr.co.fitapet.domain.domains.care.domain.QCareDate careDate;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final DateTimePath<java.time.LocalDateTime> logDate = createDateTime("logDate", java.time.LocalDateTime.class);

    public QCareLog(String variable) {
        this(CareLog.class, forVariable(variable), INITS);
    }

    public QCareLog(Path<? extends CareLog> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QCareLog(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QCareLog(PathMetadata metadata, PathInits inits) {
        this(CareLog.class, metadata, inits);
    }

    public QCareLog(Class<? extends CareLog> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.author = inits.isInitialized("author") ? new kr.co.fitapet.domain.domains.member.domain.QMember(forProperty("author"), inits.get("author")) : null;
        this.careDate = inits.isInitialized("careDate") ? new kr.co.fitapet.domain.domains.care.domain.QCareDate(forProperty("careDate"), inits.get("careDate")) : null;
    }

}

