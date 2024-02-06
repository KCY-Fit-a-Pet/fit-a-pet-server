package kr.co.fitapet.domain.domains.memo.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QMemo is a Querydsl query type for Memo
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QMemo extends EntityPathBase<Memo> {

    private static final long serialVersionUID = 1468387711L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QMemo memo = new QMemo("memo");

    public final kr.co.fitapet.domain.common.model.QAuthorAuditable _super;

    // inherited
    public final kr.co.fitapet.domain.domains.member.domain.QMember author;

    public final StringPath content = createString("content");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    // inherited
    public final kr.co.fitapet.domain.domains.member.domain.QMember lastEditorId;

    public final QMemoCategory memoCategory;

    public final ListPath<MemoImage, QMemoImage> memoImages = this.<MemoImage, QMemoImage>createList("memoImages", MemoImage.class, QMemoImage.class, PathInits.DIRECT2);

    public final StringPath title = createString("title");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt;

    public QMemo(String variable) {
        this(Memo.class, forVariable(variable), INITS);
    }

    public QMemo(Path<? extends Memo> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QMemo(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QMemo(PathMetadata metadata, PathInits inits) {
        this(Memo.class, metadata, inits);
    }

    public QMemo(Class<? extends Memo> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this._super = new kr.co.fitapet.domain.common.model.QAuthorAuditable(type, metadata, inits);
        this.author = _super.author;
        this.createdAt = _super.createdAt;
        this.lastEditorId = _super.lastEditorId;
        this.memoCategory = inits.isInitialized("memoCategory") ? new QMemoCategory(forProperty("memoCategory"), inits.get("memoCategory")) : null;
        this.updatedAt = _super.updatedAt;
    }

}

