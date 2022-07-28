package ccc.keeweapi.validator.annotations;

import ccc.keeweapi.validator.FieldsValueMismatchValidator;

import javax.validation.Constraint;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = FieldsValueMismatchValidator.class)
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface FieldsValueMismatch {
    String message() default "두 필드의 값이 달라야 합니다.";
    Class[] groups() default {};
    Class[] payload() default {};
    String from();
    String to();
}
