package kr.co.fitapet.domain.domains.care.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QCareDate is a Querydsl query type for CareDate
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QCareDate extends EntityPathBase<CareDate> {

    private static final long serialVersionUID = 673503227L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QCareDate careDate = new QCareDate("careDate");

    public final kr.co.fitapet.domain.common.model.QDateAuditable _super = new kr.co.fitapet.domain.common.model.QDateAuditable(this);

    public final QCare care;

    public final ListPath<kr.co.fitapet.domain.domains.care_log.domain.CareLog, kr.co.fitapet.domain.domains.care_log.domain.QCareLog> careLogs = this.<kr.co.fitapet.domain.domains.care_log.domain.CareLog, kr.co.fitapet.domain.domains.care_log.domain.QCareLog>createList("careLogs", kr.co.fitapet.domain.domains.care_log.domain.CareLog.class, kr.co.fitapet.domain.domains.care_log.domain.QCareLog.class, PathInits.DIRECT2);

    public final TimePath<java.time.LocalTime> careTime = createTime("careTime", java.time.LocalTime.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public final EnumPath<kr.co.fitapet.domain.domains.care.type.WeekType> week = createEnum("week", kr.co.fitapet.domain.domains.care.type.WeekType.class);

    public QCareDate(String variable) {
        this(CareDate.class, forVariable(variable), INITS);
    }

    public QCareDate(Path<? extends CareDate> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QCareDate(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QCareDate(PathMetadata metadata, PathInits inits) {
        this(CareDate.class, metadata, inits);
    }

    public QCareDate(Class<? extends CareDate> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.care = inits.isInitialized("care") ? new QCare(forProperty("care"), inits.get("care")) : null;
    }

}

