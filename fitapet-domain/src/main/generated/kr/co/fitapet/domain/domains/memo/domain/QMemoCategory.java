package kr.co.fitapet.domain.domains.memo.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QMemoCategory is a Querydsl query type for MemoCategory
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QMemoCategory extends EntityPathBase<MemoCategory> {

    private static final long serialVersionUID = 1978991517L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QMemoCategory memoCategory = new QMemoCategory("memoCategory");

    public final kr.co.fitapet.domain.common.model.QDateAuditable _super = new kr.co.fitapet.domain.common.model.QDateAuditable(this);

    public final StringPath categoryName = createString("categoryName");

    public final ListPath<MemoCategory, QMemoCategory> children = this.<MemoCategory, QMemoCategory>createList("children", MemoCategory.class, QMemoCategory.class, PathInits.DIRECT2);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final ListPath<Memo, QMemo> memos = this.<Memo, QMemo>createList("memos", Memo.class, QMemo.class, PathInits.DIRECT2);

    public final QMemoCategory parent;

    public final kr.co.fitapet.domain.domains.pet.domain.QPet pet;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QMemoCategory(String variable) {
        this(MemoCategory.class, forVariable(variable), INITS);
    }

    public QMemoCategory(Path<? extends MemoCategory> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QMemoCategory(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QMemoCategory(PathMetadata metadata, PathInits inits) {
        this(MemoCategory.class, metadata, inits);
    }

    public QMemoCategory(Class<? extends MemoCategory> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.parent = inits.isInitialized("parent") ? new QMemoCategory(forProperty("parent"), inits.get("parent")) : null;
        this.pet = inits.isInitialized("pet") ? new kr.co.fitapet.domain.domains.pet.domain.QPet(forProperty("pet")) : null;
    }

}

