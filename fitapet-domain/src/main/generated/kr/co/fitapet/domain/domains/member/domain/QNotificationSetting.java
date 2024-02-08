package kr.co.fitapet.domain.domains.member.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QNotificationSetting is a Querydsl query type for NotificationSetting
 */
@Generated("com.querydsl.codegen.DefaultEmbeddableSerializer")
public class QNotificationSetting extends BeanPath<NotificationSetting> {

    private static final long serialVersionUID = 2062141024L;

    public static final QNotificationSetting notificationSetting = new QNotificationSetting("notificationSetting");

    public final BooleanPath isCare = createBoolean("isCare");

    public final BooleanPath isMemo = createBoolean("isMemo");

    public final BooleanPath isNotice = createBoolean("isNotice");

    public final BooleanPath isSchedule = createBoolean("isSchedule");

    public QNotificationSetting(String variable) {
        super(NotificationSetting.class, forVariable(variable));
    }

    public QNotificationSetting(Path<? extends NotificationSetting> path) {
        super(path.getType(), path.getMetadata());
    }

    public QNotificationSetting(PathMetadata metadata) {
        super(NotificationSetting.class, metadata);
    }

}

