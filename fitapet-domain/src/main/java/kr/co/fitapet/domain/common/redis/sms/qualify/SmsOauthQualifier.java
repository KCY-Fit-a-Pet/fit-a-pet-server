package kr.co.fitapet.domain.common.redis.sms.qualify;

import org.springframework.beans.factory.annotation.Qualifier;

import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.PARAMETER, ElementType.METHOD,
        ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Qualifier("smsOauthQualifier")
public @interface SmsOauthQualifier {
}
