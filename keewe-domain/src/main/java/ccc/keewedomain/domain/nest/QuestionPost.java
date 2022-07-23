package ccc.keewedomain.domain.nest;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("QUESTION")
public class QuestionPost extends Post {

}
