package kr.co.fitapet.domain.domains.pet.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QPetSchedule is a Querydsl query type for PetSchedule
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QPetSchedule extends EntityPathBase<PetSchedule> {

    private static final long serialVersionUID = -1851568022L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QPetSchedule petSchedule = new QPetSchedule("petSchedule");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final DateTimePath<java.time.LocalDateTime> joinedAt = createDateTime("joinedAt", java.time.LocalDateTime.class);

    public final QPet pet;

    public final kr.co.fitapet.domain.domains.schedule.domain.QSchedule schedule;

    public QPetSchedule(String variable) {
        this(PetSchedule.class, forVariable(variable), INITS);
    }

    public QPetSchedule(Path<? extends PetSchedule> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QPetSchedule(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QPetSchedule(PathMetadata metadata, PathInits inits) {
        this(PetSchedule.class, metadata, inits);
    }

    public QPetSchedule(Class<? extends PetSchedule> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.pet = inits.isInitialized("pet") ? new QPet(forProperty("pet")) : null;
        this.schedule = inits.isInitialized("schedule") ? new kr.co.fitapet.domain.domains.schedule.domain.QSchedule(forProperty("schedule"), inits.get("schedule")) : null;
    }

}

