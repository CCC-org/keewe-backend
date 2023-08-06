package ccc.keewedomain.persistence.listener.notification;

import ccc.keewecore.consts.KeeweConsts;
import ccc.keewedomain.persistence.domain.notification.Notification;
import ccc.keewedomain.persistence.domain.notification.enums.NotificationMethod;
import ccc.keeweinfra.service.messagequeue.MQPublishService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.persistence.PostPersist;

@Slf4j
@Component
@RequiredArgsConstructor
public class NotificationEntityListener {
    private final MQPublishService mqPublishService;

    @PostPersist
    public void postPersist(Notification notification) {
        if (notification.getContents().getNotificationMethods().contains(NotificationMethod.PUSH)) {
            log.info("[TEL]::postPersist notificationId = {}", notification.getId());
            mqPublishService.publish(KeeweConsts.PUSH_SEND_EXCHANGE, notification.getId());
        }
    }
}
