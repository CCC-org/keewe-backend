package ccc.keewestatistics.listener;


import ccc.keewecore.consts.KeeweConsts;
import ccc.keewedomain.service.title.TitleStatService;
import com.rabbitmq.client.Channel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Slf4j
@RequiredArgsConstructor
public class TitleStatListener {
    private final TitleStatService titleStatService;

    @RabbitListener(queues = KeeweConsts.TITLE_STAT_QUEUE, ackMode = "MANUAL")
    void onMessage(Message message, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) long tag) throws IOException {

        try {
            titleStatService.aggregate(message);
            channel.basicAck(tag, false);
        } catch (Throwable t) {
            log.error(t.getMessage(), t);
            channel.basicNack(tag, false, false);
        }

    }

}

