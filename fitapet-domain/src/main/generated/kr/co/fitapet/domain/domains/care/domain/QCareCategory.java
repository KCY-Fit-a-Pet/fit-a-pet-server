package kr.co.fitapet.domain.domains.care.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QCareCategory is a Querydsl query type for CareCategory
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QCareCategory extends EntityPathBase<CareCategory> {

    private static final long serialVersionUID = -234768693L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QCareCategory careCategory = new QCareCategory("careCategory");

    public final kr.co.fitapet.domain.common.model.QDateAuditable _super = new kr.co.fitapet.domain.common.model.QDateAuditable(this);

    public final ListPath<Care, QCare> cares = this.<Care, QCare>createList("cares", Care.class, QCare.class, PathInits.DIRECT2);

    public final StringPath categoryName = createString("categoryName");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final kr.co.fitapet.domain.domains.pet.domain.QPet pet;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QCareCategory(String variable) {
        this(CareCategory.class, forVariable(variable), INITS);
    }

    public QCareCategory(Path<? extends CareCategory> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QCareCategory(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QCareCategory(PathMetadata metadata, PathInits inits) {
        this(CareCategory.class, metadata, inits);
    }

    public QCareCategory(Class<? extends CareCategory> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.pet = inits.isInitialized("pet") ? new kr.co.fitapet.domain.domains.pet.domain.QPet(forProperty("pet")) : null;
    }

}

