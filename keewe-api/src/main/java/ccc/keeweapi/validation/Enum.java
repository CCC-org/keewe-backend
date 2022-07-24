package ccc.keeweapi.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Constraint(validatedBy = {EnumValidator.class, EnumListValidator.class})
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface Enum {
    String message() default "ENUM 형식이 올바르지 않습니다.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
    Class<? extends java.lang.Enum<?>> enumClass();
}
