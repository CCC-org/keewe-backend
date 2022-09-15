package ccc.keeweapi.validator;

import ccc.keeweapi.validator.annotations.PostContent;
import ccc.keewecore.utils.KeeweStringUtils;
import ccc.keewedomain.domain.nest.Post;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PostContentLengthValidator implements ConstraintValidator<PostContent, String> {
    // TODO KeeweException 던지도록 수정하기
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        long length = KeeweStringUtils.getGraphemeLength(value);
        return length > 0 && length <= Post.CONTENT_MAX_LENGTH;
    }
}
