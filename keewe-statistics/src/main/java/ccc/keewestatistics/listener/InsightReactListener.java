package ccc.keewestatistics.listener;

import ccc.keewecore.consts.KeeweConsts;
import ccc.keewedomain.dto.insight.ReactionIncrementDto;
import ccc.keewedomain.service.insight.ReactionDomainService;
import com.rabbitmq.client.Channel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class InsightReactListener {
    private final ReactionDomainService reactionDomainService;

    @RabbitListener(queues = KeeweConsts.INSIGHT_REACT_QUEUE, ackMode = "MANUAL")
    public void onMessage(ReactionIncrementDto dto, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) long tag) throws IOException {
        try {
            log.info("[InsightReactListener] 리액션 증가 이벤트 - insightId({})", dto.getInsightId());
            reactionDomainService.applyReact(dto);
            channel.basicAck(tag, false);
        } catch (Throwable t) {
            log.error(t.getMessage(), t);
            channel.basicNack(tag, false, false);
        }
    }
}
