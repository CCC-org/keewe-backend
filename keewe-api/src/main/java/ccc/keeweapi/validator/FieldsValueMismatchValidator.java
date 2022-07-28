package ccc.keeweapi.validator;

import ccc.keeweapi.validator.annotations.FieldsValueMismatch;
import org.springframework.beans.BeanWrapperImpl;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class FieldsValueMismatchValidator implements ConstraintValidator<FieldsValueMismatch, Object> {

    private String from;
    private String to;

    @Override
    public void initialize(FieldsValueMismatch constraintAnnotation) {
        this.from = constraintAnnotation.from();
        this.to = constraintAnnotation.to();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        BeanWrapperImpl beanWrapper = new BeanWrapperImpl(value);
        Object fromValue = beanWrapper.getPropertyValue(from);
        Object toValue = beanWrapper.getPropertyValue(to);

        if (fromValue != null) {
            return !fromValue.equals(toValue);
        }
        return toValue != null;
    }
}
