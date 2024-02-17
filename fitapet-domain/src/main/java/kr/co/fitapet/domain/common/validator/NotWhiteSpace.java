package kr.co.fitapet.domain.common.validator;

import jakarta.validation.Constraint;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 문자열 전체에 공백이 포함되어 있지 않은지 검증하는 애노테이션 <br>
 * null은 유효하다고 판단한다.
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = NotWhiteSpaceValidator.class)
public @interface NotWhiteSpace {
    String message() default "whitespace is not allowed";

    Class<?>[] groups() default {};

    Class<?>[] payload() default {};
}
