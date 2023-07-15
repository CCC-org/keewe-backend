package ccc.keeweapi.service.notification;

import ccc.keeweapi.dto.notification.NotificationResponse;
import ccc.keewedomain.persistence.domain.notification.Notification;
import ccc.keewedomain.persistence.domain.notification.enums.NotificationCategory;
import ccc.keewedomain.persistence.domain.notification.enums.NotificationContents;
import org.springframework.stereotype.Component;

@Component
public class TitleNotificationProcessor implements NotificationProcessor {
    @Override
    public NotificationCategory getCategory() {
        return NotificationCategory.TITLE;
    }

    @Override
    public NotificationResponse process(Notification notification) {
        NotificationContents contents = notification.getContents();
        return NotificationResponse.of(
            notification.getId(),
            contents.getTitle(),
            contents.getContents(),
            contents.getCategory(),
            notification.getReferenceId(),
            notification.isRead(),
            notification.getCreatedAt().toString()
        );
    }
}
