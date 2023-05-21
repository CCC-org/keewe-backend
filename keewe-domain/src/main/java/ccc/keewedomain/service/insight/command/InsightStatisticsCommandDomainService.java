package ccc.keewedomain.service.insight.command;

import ccc.keewecore.consts.KeeweConsts;
import ccc.keewedomain.event.insight.ProfileVisitFromInsightEvent;
import ccc.keeweinfra.service.messagequeue.MQPublishService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InsightStatisticsCommandDomainService {
    private final MQPublishService mqPublishService;

    public void publishProfileVisitFromInsightEvent(Long userId, Long insightId) {
        ProfileVisitFromInsightEvent event = ProfileVisitFromInsightEvent.of(userId, insightId);
        mqPublishService.publish(KeeweConsts.PROFILE_VISIT_FROM_INSIGHT_EXCHANGE, event);
    }
}
