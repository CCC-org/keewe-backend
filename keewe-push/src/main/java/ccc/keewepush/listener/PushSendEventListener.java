package ccc.keewepush.listener;

import ccc.keewecore.consts.KeeweConsts;
import ccc.keewedomain.event.push.PushSendEvent;
import ccc.keewepush.service.PushService;
import com.rabbitmq.client.Channel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;

import java.io.IOException;

@RabbitListener
@Slf4j
@RequiredArgsConstructor
public class PushSendEventListener {
    private final PushService pushService;

    @RabbitListener(queues = KeeweConsts.PUSH_SEND_QUEUE, ackMode = "MANUAL")
    public void onMessage(PushSendEvent event, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) long tag) throws IOException {
        try {
            log.info("[PushSendEventListener] 푸시 알림 전송 이벤트 - targetUserId = [{}], content = [{}]", event.getUserId(), event.getContent());
            pushService.sendPushByUserId(event.getUserId(), event.getTitle(), event.getContent());
            channel.basicAck(tag, false);
        } catch (Throwable t) {
            log.error(t.getMessage(), t);
            channel.basicNack(tag, false, false);
        }
    }
}
