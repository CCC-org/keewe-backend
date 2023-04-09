package ccc.keeweapi.service.notification.command;

import ccc.keeweapi.dto.notification.NotificationResponse;
import ccc.keeweapi.service.notification.NotificationProcessor;
import ccc.keewedomain.persistence.domain.notification.Notification;
import ccc.keewedomain.persistence.domain.notification.enums.NotificationCategory;
import ccc.keewedomain.service.notification.command.NotificationCommandDomainService;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class NotificationCommandApiService {
    private final NotificationCommandDomainService notificationCommandDomainService;
    private final Map<NotificationCategory, NotificationProcessor> notificationProcessors;

    public NotificationCommandApiService(
            NotificationCommandDomainService notificationCommandDomainService,
            List<NotificationProcessor> notificationProcessors
    ) {
        this.notificationCommandDomainService = notificationCommandDomainService;
        this.notificationProcessors = notificationProcessors.stream()
                .collect(Collectors.toMap(NotificationProcessor::getCategory, notificationProcessor -> notificationProcessor));
    }

    public NotificationResponse markAsRead(Long notificationId) {
        Notification notification = notificationCommandDomainService.markAsRead(notificationId);
        return notificationProcessors.get(notification.getContents().getCategory())
                .process(notification);
    }
}
