package ccc.keeweapi.service.notification;


import ccc.keeweapi.dto.notification.NotificationResponse;
import ccc.keewedomain.persistence.domain.notification.Notification;
import ccc.keewedomain.persistence.domain.notification.enums.NotificationCategory;

public interface NotificationProcessor {
    NotificationCategory getCategory();
    NotificationResponse process(Notification notification);
}
