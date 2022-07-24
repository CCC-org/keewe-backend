package ccc.keeweapi.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.List;

public class EnumListValidator implements ConstraintValidator<Enum, List<String>> {
    private Enum annotation;

    @Override
    public void initialize(Enum constraintAnnotation) {
        this.annotation = constraintAnnotation;
    }

    @Override
    public boolean isValid(List<String> values, ConstraintValidatorContext context) {
        Object[] enumValues = this.annotation.enumClass().getEnumConstants();
        if (values == null || enumValues == null)
            return true;

        for (String value : values) {
            boolean result = false;
            for (Object enumValue : enumValues) {
                if (value.equals(enumValue.toString()))
                    return true;
            }
            if (!result)
                return result;
        }

        return true;
    }
}
