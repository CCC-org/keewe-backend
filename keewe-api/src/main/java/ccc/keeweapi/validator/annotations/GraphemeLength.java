package ccc.keeweapi.validator.annotations;

import ccc.keeweapi.validator.GraphemeLengthValidator;

import javax.validation.Constraint;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = GraphemeLengthValidator.class)
public @interface GraphemeLength {
    String message() default "길이 조건과 일치하지 않습니다.";
    Class[] groups() default {};
    Class[] payload() default {};
    int min() default 0;
    int max() default Integer.MAX_VALUE;
}
