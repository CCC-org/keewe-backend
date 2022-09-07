package ccc.keewedomain.domain.nest;

import ccc.keewedomain.domain.user.User;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("QUESTION")
public class QuestionPost extends Post {

    protected QuestionPost(User writer, String content) {
        super(writer, content);
    }

    public static QuestionPost of(User profile, String content) {
        return new QuestionPost(profile, content);
    }
}
