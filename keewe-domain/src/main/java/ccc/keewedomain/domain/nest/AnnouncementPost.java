package ccc.keewedomain.domain.nest;

import ccc.keewedomain.domain.user.Profile;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("ANNOUNCEMENT")
public class AnnouncementPost extends Post {

    protected AnnouncementPost(Nest nest, Profile writer, String content) {
        super(nest, writer, content);
    }

    public static AnnouncementPost of(Profile writer, String content) {
        return new AnnouncementPost(writer.getNest(), writer, content);
    }
}
