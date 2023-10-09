package ccc.keewepush.service.notification;

import ccc.keewedomain.persistence.domain.notification.Notification;
import ccc.keewedomain.persistence.domain.notification.enums.NotificationCategory;
import ccc.keewepush.dto.PushMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class TitlePushNotificationProcessor implements PushNotificationProcessor {
    @Override
    public NotificationCategory getCategory() {
        return NotificationCategory.TITLE;
    }

    @Override
    @Transactional(readOnly = true)
    public PushMessage process(Notification notification) {
        return PushMessage.of(notification.getUser().getId(), notification.getContents().getPushContents());
    }
}
