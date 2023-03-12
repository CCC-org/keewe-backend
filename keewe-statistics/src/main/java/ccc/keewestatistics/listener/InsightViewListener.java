package ccc.keewestatistics.listener;

import ccc.keewecore.consts.KeeweConsts;
import ccc.keewedomain.service.insight.command.InsightCommandDomainService;
import com.rabbitmq.client.Channel;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class InsightViewListener {
    private final InsightCommandDomainService insightCommandDomainService;

    @RabbitListener(queues = KeeweConsts.INSIGHT_VIEW_QUEUE, ackMode = "MANUAL")
    public void onMessage(Message message, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) long tag) throws IOException {
        try {
            Long insightId = Long.parseLong(new String(message.getBody()));

            log.info("[IVL::onMessage] insightId {}", insightId);
            insightCommandDomainService.incrementViewCount(insightId);
            channel.basicAck(tag, false);
        } catch (Throwable t) {
            log.error(t.getMessage(), t);
            channel.basicNack(tag, false, false);
        }
    }
}
