package ccc.keewestatistics.listener;

import ccc.keewecore.consts.KeeweConsts;
import ccc.keewecore.consts.KeeweRtnConsts;
import ccc.keewecore.exception.KeeweException;
import ccc.keewedomain.dto.user.FollowFromInsightCreateDto;
import ccc.keewedomain.event.follow.FollowCreateEvent;
import ccc.keewedomain.service.insight.command.InsightStatisticsCommandDomainService;
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
public class FollowFromInsightListener {
    private final InsightStatisticsCommandDomainService insightStatisticsCommandDomainService;

    @RabbitListener(queues = KeeweConsts.FOLLOW_FROM_INSIGHT_QUEUE, ackMode = "MANUAL")
    public void onMessage(FollowCreateEvent event, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) long tag) throws IOException {
        Long followerId = event.getFollowerId();
        Long followeeId = event.getFolloweeId();
        log.info("[FFIL::onMessage] FollowFromInsight event consuming - insightId({}), followerId({}), followeeId({})",
                event.getRefInsightId(), followerId, followeeId);
        try {
            FollowFromInsightCreateDto dto = FollowFromInsightCreateDto.of(followerId, followeeId, event.getRefInsightId());
            insightStatisticsCommandDomainService.createFollowFromInsight(dto);
            channel.basicAck(tag, true);
        } catch (KeeweException keeweException) {
            if(keeweException.getKeeweRtnConsts() == KeeweRtnConsts.ERR428) {
                channel.basicAck(tag, true);
                return;
            }
            log.error(keeweException.getMessage(), keeweException);
            channel.basicNack(tag, false, false);
        } catch (Throwable t) {
            log.error(t.getMessage(), t);
            channel.basicNack(tag, false, false);
        }
    }
}
