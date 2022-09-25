package ccc.keewestatistics.listener;

import ccc.keewecore.consts.KeeweConsts;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class InsightViewListener {

    @RabbitListener(queues = KeeweConsts.INSIGHT_VIEW_QUEUE)
    public void onMessage(Message message, Channel channel) {
        //Long insightId = (Long) message.getBody();
        log.info("message {}", message.toString());
    }
}
