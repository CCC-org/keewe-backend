package ccc.keeweapi.validator.nest;

import javax.validation.Constraint;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PostContentLengthValidator.class)
public @interface PostContent {
    String message() default "내용의 길이가 초과됐거나 비어있어요";
    Class[] groups() default {};
    Class[] payload() default {};
}
