package ccc.keewedomain.domain.nest;

import ccc.keewedomain.domain.user.Profile;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("QUESTION")
public class QuestionPost extends Post {

    protected QuestionPost(Nest nest, Profile writer, String content) {
        super(nest, writer, content);
    }
}
