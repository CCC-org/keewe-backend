package ccc.keeweapi.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class EnumValidator implements ConstraintValidator<Enum, String> {
    private Enum annotation;

    @Override
    public void initialize(Enum constraintAnnotation) {
        this.annotation = constraintAnnotation;
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        Object[] enumValues = this.annotation.enumClass().getEnumConstants();
        boolean result = false;
        if (value == null || enumValues == null)
            return true;

        for (Object enumValue : enumValues) {
            if (value.equals(enumValue.toString()))
                result = true;
        }
        return result;
    }
}
