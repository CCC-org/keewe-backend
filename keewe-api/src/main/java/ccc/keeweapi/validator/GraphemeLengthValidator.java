package ccc.keeweapi.validator;

import ccc.keeweapi.validator.annotations.GraphemeLength;
import ccc.keewecore.utils.StringLengthUtil;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class GraphemeLengthValidator implements ConstraintValidator<GraphemeLength, String> {

    private int min;
    private int max;

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return min == 0;
        }
        int length = (int) StringLengthUtil.getGraphemeLength(value);
        return length >= min && length <= max;
    }

    @Override
    public void initialize(GraphemeLength constraintAnnotation) {
        min = constraintAnnotation.min();
        max = constraintAnnotation.max();
        validateParameters();
    }

    private void validateParameters() {
        if (min < 0) {
            throw new IllegalArgumentException("min은 음수일 수 없습니다.");
        }
        if (max < 0) {
            throw new IllegalArgumentException("max는 음수일 수 없습니다.");
        }
        if(min > max) {
            throw new IllegalArgumentException("max는 min보다 커야합니다.");
        }
    }
}
