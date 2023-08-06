package ccc.keewepush.listener;

import ccc.keewecore.consts.KeeweConsts;
import ccc.keewepush.service.notification.NotificationPushService;
import com.rabbitmq.client.Channel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Slf4j
@RequiredArgsConstructor
public class PushNotificationListener {
    private final NotificationPushService notificationPushService;

    @RabbitListener(queues = KeeweConsts.PUSH_SEND_QUEUE, ackMode = "MANUAL")
    public void onMessage(Long notificationId, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) long tag) throws IOException {
        try {
            log.info("[PushSendEventListener] 푸시 알림 전송 이벤트 - notificationId = [{}]", notificationId);
            notificationPushService.sendPushNotification(notificationId);
            channel.basicAck(tag, false);
        } catch (Throwable t) {
            log.error(t.getMessage(), t);
            channel.basicNack(tag, false, false);
        }
    }
}
