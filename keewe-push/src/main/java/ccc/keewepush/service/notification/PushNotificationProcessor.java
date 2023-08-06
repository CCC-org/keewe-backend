package ccc.keewepush.service.notification;

import ccc.keewedomain.persistence.domain.notification.Notification;
import ccc.keewedomain.persistence.domain.notification.enums.NotificationCategory;
import ccc.keewepush.dto.PushMessage;

public interface PushNotificationProcessor {
    NotificationCategory getCategory();
    PushMessage process(Notification notification);
}
