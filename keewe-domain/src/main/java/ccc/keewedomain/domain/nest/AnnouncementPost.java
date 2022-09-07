package ccc.keewedomain.domain.nest;

import ccc.keewedomain.domain.user.User;

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
