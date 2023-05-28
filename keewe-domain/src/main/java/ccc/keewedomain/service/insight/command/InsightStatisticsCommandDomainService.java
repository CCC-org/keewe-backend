package ccc.keewedomain.service.insight.command;

import ccc.keewecore.consts.KeeweConsts;
import ccc.keewecore.consts.KeeweRtnConsts;
import ccc.keewecore.exception.KeeweException;
import ccc.keewedomain.event.insight.ProfileVisitFromInsightEvent;
import ccc.keewedomain.persistence.domain.insight.Insight;
import ccc.keewedomain.persistence.domain.insight.ProfileVisitFromInsight;
import ccc.keewedomain.persistence.domain.insight.id.ProfileVisitFromInsightId;
import ccc.keewedomain.persistence.domain.user.User;
import ccc.keewedomain.persistence.repository.insight.ProfileVisitFromInsightRepository;
import ccc.keewedomain.service.insight.query.InsightQueryDomainService;
import ccc.keewedomain.service.user.UserDomainService;
import ccc.keeweinfra.service.messagequeue.MQPublishService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InsightStatisticsCommandDomainService {
    private final MQPublishService mqPublishService;
    private final ProfileVisitFromInsightRepository profileVisitFromInsightRepository;
    private final UserDomainService userDomainService;
    private final InsightQueryDomainService insightQueryDomainService;

    public void publishProfileVisitFromInsightEvent(Long userId, Long insightId) {
        ProfileVisitFromInsightEvent event = ProfileVisitFromInsightEvent.of(insightId, userId);
        mqPublishService.publish(KeeweConsts.PROFILE_VISIT_FROM_INSIGHT_EXCHANGE, event);
    }

    public ProfileVisitFromInsight createProfileVisitFromInsight(Long insightId, Long userId) {
        ProfileVisitFromInsightId id = ProfileVisitFromInsightId.of(insightId, userId);
        if(profileVisitFromInsightRepository.existsById(id)) {
            throw new KeeweException(KeeweRtnConsts.ERR490);
        }

        Insight insight = insightQueryDomainService.getByIdOrElseThrow(insightId);
        User user = userDomainService.getUserByIdOrElseThrow(userId);
        ProfileVisitFromInsight profileVisitFromInsight = ProfileVisitFromInsight.of(insight, user);
        return profileVisitFromInsightRepository.save(profileVisitFromInsight);
    }
}
