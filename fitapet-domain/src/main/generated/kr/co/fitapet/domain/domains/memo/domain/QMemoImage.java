package kr.co.fitapet.domain.domains.memo.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QMemoImage is a Querydsl query type for MemoImage
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QMemoImage extends EntityPathBase<MemoImage> {

    private static final long serialVersionUID = 358874908L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QMemoImage memoImage = new QMemoImage("memoImage");

    public final kr.co.fitapet.domain.common.model.QDateAuditable _super = new kr.co.fitapet.domain.common.model.QDateAuditable(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath imgUrl = createString("imgUrl");

    public final QMemo memo;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QMemoImage(String variable) {
        this(MemoImage.class, forVariable(variable), INITS);
    }

    public QMemoImage(Path<? extends MemoImage> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QMemoImage(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QMemoImage(PathMetadata metadata, PathInits inits) {
        this(MemoImage.class, metadata, inits);
    }

    public QMemoImage(Class<? extends MemoImage> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.memo = inits.isInitialized("memo") ? new QMemo(forProperty("memo"), inits.get("memo")) : null;
    }

}

