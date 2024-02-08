package kr.co.fitapet.domain.domains.schedule.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QSchedule is a Querydsl query type for Schedule
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QSchedule extends EntityPathBase<Schedule> {

    private static final long serialVersionUID = 1324939321L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QSchedule schedule = new QSchedule("schedule");

    public final kr.co.fitapet.domain.common.model.QAuthorAuditable _super;

    // inherited
    public final kr.co.fitapet.domain.domains.member.domain.QMember author;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    // inherited
    public final kr.co.fitapet.domain.domains.member.domain.QMember lastEditorId;

    public final StringPath location = createString("location");

    public final DateTimePath<java.time.LocalDateTime> notifyDt = createDateTime("notifyDt", java.time.LocalDateTime.class);

    public final ListPath<kr.co.fitapet.domain.domains.pet.domain.PetSchedule, kr.co.fitapet.domain.domains.pet.domain.QPetSchedule> pets = this.<kr.co.fitapet.domain.domains.pet.domain.PetSchedule, kr.co.fitapet.domain.domains.pet.domain.QPetSchedule>createList("pets", kr.co.fitapet.domain.domains.pet.domain.PetSchedule.class, kr.co.fitapet.domain.domains.pet.domain.QPetSchedule.class, PathInits.DIRECT2);

    public final DateTimePath<java.time.LocalDateTime> reservationDt = createDateTime("reservationDt", java.time.LocalDateTime.class);

    public final StringPath scheduleName = createString("scheduleName");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt;

    public QSchedule(String variable) {
        this(Schedule.class, forVariable(variable), INITS);
    }

    public QSchedule(Path<? extends Schedule> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QSchedule(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QSchedule(PathMetadata metadata, PathInits inits) {
        this(Schedule.class, metadata, inits);
    }

    public QSchedule(Class<? extends Schedule> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this._super = new kr.co.fitapet.domain.common.model.QAuthorAuditable(type, metadata, inits);
        this.author = _super.author;
        this.createdAt = _super.createdAt;
        this.lastEditorId = _super.lastEditorId;
        this.updatedAt = _super.updatedAt;
    }

}

