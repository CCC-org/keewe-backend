package ccc.keewedomain.domain.nest;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("ANNOUNCEMENT")
public class AnnouncementPost extends Post {
}
