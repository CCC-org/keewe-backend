package ccc.keewestatistics.listener;

import ccc.keewecore.consts.KeeweConsts;
import ccc.keewedomain.dto.user.FollowFromInsightDto;
import ccc.keewedomain.persistence.domain.user.FollowFromInsight;
import ccc.keewedomain.service.user.command.ProfileCommandDomainService;
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
    private final ProfileCommandDomainService profileCommandDomainService;

    @RabbitListener(queues = KeeweConsts.FOLLOW_FROM_INSIGHT_QUEUE, ackMode = "MANUAL")
    public void onMessage(FollowFromInsightDto dto, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) long tag) throws IOException {
        log.info("[FFIL::onMessage] insightId {} followerId {} followeeId {}", dto.getInsightId(), dto.getFollowerId(), dto.getFolloweeId());
        try {
            profileCommandDomainService.addFollowFromInsight(dto);
            channel.basicAck(tag, true);
        } catch (Throwable t) {
            log.error(t.getMessage());
            channel.basicNack(tag, false, false);
        }
    }
}
