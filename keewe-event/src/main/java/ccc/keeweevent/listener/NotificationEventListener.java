package ccc.keeweevent.listener;

import ccc.keewecore.consts.KeeweConsts;
import ccc.keewedomain.event.notification.NotificationCreateEvent;
import ccc.keewedomain.persistence.domain.notification.Notification;
import ccc.keewedomain.persistence.domain.notification.enums.NotificationMethod;
import ccc.keeweinfra.service.messagequeue.MQPublishService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@Slf4j
@RequiredArgsConstructor
public class NotificationEventListener {
    private final MQPublishService mqPublishService;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT, fallbackExecution = true)
    @Async(value = "worker")
    public void handleEvent(NotificationCreateEvent event) {
        Notification notification = event.getNotification();
        if (notification.getContents().getNotificationMethods().contains(NotificationMethod.PUSH)) {
            log.info("[NotificationEventListener] 푸시 알림 생성 이벤트 - notificationId({})", notification.getId());
            mqPublishService.publish(KeeweConsts.PUSH_SEND_EXCHANGE, notification.getId());
        }
    }
}
