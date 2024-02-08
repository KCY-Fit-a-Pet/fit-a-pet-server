package kr.co.fitapet.domain.domains.pet.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QPet is a Querydsl query type for Pet
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QPet extends EntityPathBase<Pet> {

    private static final long serialVersionUID = 235191475L;

    public static final QPet pet = new QPet("pet");

    public final kr.co.fitapet.domain.common.model.QDateAuditable _super = new kr.co.fitapet.domain.common.model.QDateAuditable(this);

    public final DatePath<java.time.LocalDate> birthdate = createDate("birthdate", java.time.LocalDate.class);

    public final ListPath<kr.co.fitapet.domain.domains.care.domain.CareCategory, kr.co.fitapet.domain.domains.care.domain.QCareCategory> careCategories = this.<kr.co.fitapet.domain.domains.care.domain.CareCategory, kr.co.fitapet.domain.domains.care.domain.QCareCategory>createList("careCategories", kr.co.fitapet.domain.domains.care.domain.CareCategory.class, kr.co.fitapet.domain.domains.care.domain.QCareCategory.class, PathInits.DIRECT2);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final StringPath feed = createString("feed");

    public final EnumPath<kr.co.fitapet.domain.domains.pet.type.GenderType> gender = createEnum("gender", kr.co.fitapet.domain.domains.pet.type.GenderType.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final ListPath<kr.co.fitapet.domain.domains.member.domain.Manager, kr.co.fitapet.domain.domains.member.domain.QManager> managers = this.<kr.co.fitapet.domain.domains.member.domain.Manager, kr.co.fitapet.domain.domains.member.domain.QManager>createList("managers", kr.co.fitapet.domain.domains.member.domain.Manager.class, kr.co.fitapet.domain.domains.member.domain.QManager.class, PathInits.DIRECT2);

    public final ListPath<kr.co.fitapet.domain.domains.memo.domain.MemoCategory, kr.co.fitapet.domain.domains.memo.domain.QMemoCategory> memoCategories = this.<kr.co.fitapet.domain.domains.memo.domain.MemoCategory, kr.co.fitapet.domain.domains.memo.domain.QMemoCategory>createList("memoCategories", kr.co.fitapet.domain.domains.memo.domain.MemoCategory.class, kr.co.fitapet.domain.domains.memo.domain.QMemoCategory.class, PathInits.DIRECT2);

    public final BooleanPath neutered = createBoolean("neutered");

    public final StringPath petName = createString("petName");

    public final StringPath petProfileImg = createString("petProfileImg");

    public final ListPath<PetSchedule, QPetSchedule> schedules = this.<PetSchedule, QPetSchedule>createList("schedules", PetSchedule.class, QPetSchedule.class, PathInits.DIRECT2);

    public final StringPath species = createString("species");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QPet(String variable) {
        super(Pet.class, forVariable(variable));
    }

    public QPet(Path<? extends Pet> path) {
        super(path.getType(), path.getMetadata());
    }

    public QPet(PathMetadata metadata) {
        super(Pet.class, metadata);
    }

}

