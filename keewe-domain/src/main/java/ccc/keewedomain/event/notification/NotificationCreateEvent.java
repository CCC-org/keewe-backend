package ccc.keewedomain.event.notification;

import ccc.keewedomain.persistence.domain.notification.Notification;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor(staticName = "of")
public class NotificationCreateEvent implements Serializable {
    private Notification notification;
}
