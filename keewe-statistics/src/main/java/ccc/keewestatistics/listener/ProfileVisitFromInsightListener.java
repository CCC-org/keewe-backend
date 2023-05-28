package ccc.keewestatistics.listener;

import ccc.keewecore.consts.KeeweConsts;
import ccc.keewecore.consts.KeeweRtnConsts;
import ccc.keewecore.exception.KeeweException;
import ccc.keewedomain.event.insight.ProfileVisitFromInsightEvent;
import ccc.keewedomain.service.insight.command.InsightStatisticsCommandDomainService;
import com.rabbitmq.client.Channel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class ProfileVisitFromInsightListener {
    private final InsightStatisticsCommandDomainService insightStatisticsCommandDomainService;

    @RabbitListener(queues = KeeweConsts.PROFILE_VISIT_FROM_INSIGHT_QUEUE, ackMode = "MANUAL")
    public void onMessage(ProfileVisitFromInsightEvent event, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) long tag) throws IOException {
        log.info("[PVFIL::onMessage] ProfileVisitFromInsight event consuming - insightId ({}), userId ({})",
                event.getInsightId(), event.getUserId());
        try {
            insightStatisticsCommandDomainService.createProfileVisitFromInsight(event.getInsightId(), event.getUserId());
        } catch (KeeweException keeweException) {
            if(keeweException.getKeeweRtnConsts() == KeeweRtnConsts.ERR490) {
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
