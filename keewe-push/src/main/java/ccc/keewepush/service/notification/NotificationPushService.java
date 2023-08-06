package ccc.keewepush.service.notification;

import ccc.keewedomain.persistence.domain.notification.Notification;
import ccc.keewedomain.persistence.domain.notification.enums.NotificationCategory;
import ccc.keewedomain.service.notification.query.NotificationQueryDomainService;
import ccc.keewepush.dto.PushMessage;
import ccc.keewepush.service.push.PushService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Slf4j
public class NotificationPushService {
    private final NotificationQueryDomainService notificationQueryDomainService;
    private final PushService pushService;
    private final Map<NotificationCategory, PushNotificationProcessor> pushNotificationProcessors;

    public NotificationPushService(NotificationQueryDomainService notificationQueryDomainService, PushService pushService, List<PushNotificationProcessor> processors) {
        this.notificationQueryDomainService = notificationQueryDomainService;
        this.pushService = pushService;
        this.pushNotificationProcessors = processors.stream()
                .collect(Collectors.toMap(PushNotificationProcessor::getCategory, processor -> processor));
    }

    public void sendPushNotification(Long notificationId) {
        Notification notification = notificationQueryDomainService.findByIdOrElseThrow(notificationId);
        PushNotificationProcessor processor = pushNotificationProcessors.get(notification.getContents().getCategory());

        if (Objects.isNull(processor)) {
            log.warn("[NotificationPushService] category = [{}]의 processor가 구현되지 않았습니다.", notification.getContents().getCategory());
            return;
        }

        PushMessage pushMessage = processor
                .process(notification);
        pushService.sendPush(pushMessage);
    }
}
