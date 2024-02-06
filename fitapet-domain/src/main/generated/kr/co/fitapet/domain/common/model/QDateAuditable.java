package kr.co.fitapet.domain.common.model;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QDateAuditable is a Querydsl query type for DateAuditable
 */
@Generated("com.querydsl.codegen.DefaultSupertypeSerializer")
public class QDateAuditable extends EntityPathBase<DateAuditable> {

    private static final long serialVersionUID = 646402319L;

    public static final QDateAuditable dateAuditable = new QDateAuditable("dateAuditable");

    public final DateTimePath<java.time.LocalDateTime> createdAt = createDateTime("createdAt", java.time.LocalDateTime.class);

    public final DateTimePath<java.time.LocalDateTime> updatedAt = createDateTime("updatedAt", java.time.LocalDateTime.class);

    public QDateAuditable(String variable) {
        super(DateAuditable.class, forVariable(variable));
    }

    public QDateAuditable(Path<? extends DateAuditable> path) {
        super(path.getType(), path.getMetadata());
    }

    public QDateAuditable(PathMetadata metadata) {
        super(DateAuditable.class, metadata);
    }

}

