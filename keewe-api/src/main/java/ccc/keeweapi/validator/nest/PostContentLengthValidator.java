package ccc.keeweapi.validator.nest;

import ccc.keewecore.utils.StringLengthUtil;
import ccc.keewedomain.domain.nest.Post;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PostContentLengthValidator implements ConstraintValidator<PostContent, String> {
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        long length = StringLengthUtil.getGraphemeLength(value);
        return length > 0 && length <= Post.CONTENT_MAX_LENGTH;
    }
}
