package ccc.keeweapi.validator;

import ccc.keeweapi.validator.annotations.FieldsValueCompare;
import org.springframework.beans.BeanWrapperImpl;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Objects;

public class FieldsValueMismatchValidator implements ConstraintValidator<FieldsValueCompare, Object> {

    private String from;
    private String to;
    private boolean match;

    @Override
    public void initialize(FieldsValueCompare constraintAnnotation) {
        this.from = constraintAnnotation.from();
        this.to = constraintAnnotation.to();
        this.match = constraintAnnotation.match();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        BeanWrapperImpl beanWrapper = new BeanWrapperImpl(value);
        Object fromValue = beanWrapper.getPropertyValue(from);
        Object toValue = beanWrapper.getPropertyValue(to);

        boolean result = Objects.equals(fromValue, toValue);

        return match ? result : !result;
    }
}
