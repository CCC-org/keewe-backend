package ccc.keewedomain.persistence.domain.nest;

import ccc.keewedomain.persistence.domain.user.User;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("ANNOUNCEMENT")
public class AnnouncementPost extends Post {

    protected AnnouncementPost(User writer, String content) {
        super(writer, content);
    }

    public static AnnouncementPost of(User writer, String content) {
        return new AnnouncementPost(writer, content);
    }
}
