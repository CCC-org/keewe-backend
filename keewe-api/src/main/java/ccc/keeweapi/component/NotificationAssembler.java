package ccc.keeweapi.component;

import ccc.keeweapi.dto.notification.UnreadNotificationExistenceResponse;
import org.springframework.stereotype.Component;

@Component
public class NotificationAssembler {
    public UnreadNotificationExistenceResponse toUnreadNotificationExistenceResponse(Boolean exist) {
        return UnreadNotificationExistenceResponse.of(exist);
    }
}
