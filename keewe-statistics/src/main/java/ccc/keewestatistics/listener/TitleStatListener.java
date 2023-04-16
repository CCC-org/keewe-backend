package ccc.keewestatistics.listener;


import ccc.keewecore.consts.KeeweConsts;
import ccc.keewecore.consts.TitleCategory;
import ccc.keewecore.utils.KeeweTitleHeader;
import ccc.keewedomain.service.title.AbstractTitleAcquireProcessor;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@Slf4j
public class TitleStatListener {
    private final Map<TitleCategory, AbstractTitleAcquireProcessor> statServiceMap;

    public TitleStatListener(List<AbstractTitleAcquireProcessor> abstractTitleAcquireProcessors) {
        statServiceMap = abstractTitleAcquireProcessors.stream().collect(Collectors.toMap(AbstractTitleAcquireProcessor::getProcessableCategory, v -> v));
    }

    @RabbitListener(queues = KeeweConsts.TITLE_STAT_QUEUE, ackMode = "MANUAL")
    void onMessage(Message message, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) long tag) throws IOException {
        try {
            KeeweTitleHeader header = KeeweTitleHeader.toHeader(message);
            log.info("[TSL::onMessage] Title event consuming. category={}, userId={}", header.getCategory(), header.getUserId());
            statServiceMap.get(header.getCategory()).aggregateStat(header);
            channel.basicAck(tag, false);
        } catch (Throwable t) {
            log.error(t.getMessage(), t);
            channel.basicNack(tag, false, false);
        }
    }
}