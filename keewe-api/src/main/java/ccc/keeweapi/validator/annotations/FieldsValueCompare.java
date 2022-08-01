package ccc.keeweapi.validator.annotations;

import ccc.keeweapi.validator.FieldsValueMismatchValidator;

import javax.validation.Constraint;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * from, to 비교할 두 필드의 이름
 * match 기대 비교 결과 true: 일치, false: 불일치
 */

@Constraint(validatedBy = FieldsValueMismatchValidator.class)
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface FieldsValueCompare {
    String message() default "기대한 비교 결과와 일치하지 않습니다.";
    Class[] groups() default {};
    Class[] payload() default {};
    String from();
    String to();
    boolean match();
}
