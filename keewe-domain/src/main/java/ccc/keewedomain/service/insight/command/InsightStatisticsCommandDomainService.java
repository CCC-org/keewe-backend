package ccc.keewedomain.service.insight.command;

import ccc.keewecore.consts.KeeweConsts;
import ccc.keewecore.consts.KeeweRtnConsts;
import ccc.keewecore.exception.KeeweException;
import ccc.keewedomain.dto.user.FollowFromInsightCreateDto;
import ccc.keewedomain.event.insight.ProfileVisitFromInsightEvent;
import ccc.keewedomain.persistence.domain.insight.FollowFromInsight;
import ccc.keewedomain.persistence.domain.insight.Insight;
import ccc.keewedomain.persistence.domain.insight.ProfileVisitFromInsight;
import ccc.keewedomain.persistence.domain.insight.id.FollowFromInsightId;
import ccc.keewedomain.persistence.domain.insight.id.ProfileVisitFromInsightId;
import ccc.keewedomain.persistence.domain.user.User;
import ccc.keewedomain.persistence.repository.insight.FollowFromInsightRepository;
import ccc.keewedomain.persistence.repository.insight.ProfileVisitFromInsightRepository;
import ccc.keewedomain.service.insight.query.InsightQueryDomainService;
import ccc.keewedomain.service.user.UserDomainService;
import ccc.keeweinfra.service.messagequeue.MQPublishService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class InsightStatisticsCommandDomainService {
    private final MQPublishService mqPublishService;
    private final ProfileVisitFromInsightRepository profileVisitFromInsightRepository;
    private final UserDomainService userDomainService;
    private final InsightQueryDomainService insightQueryDomainService;
    private final FollowFromInsightRepository followFromInsightRepository;

    public void publishProfileVisitFromInsightEvent(Long userId, Long insightId) {
        ProfileVisitFromInsightEvent event = ProfileVisitFromInsightEvent.of(insightId, userId);
        mqPublishService.publish(KeeweConsts.PROFILE_VISIT_FROM_INSIGHT_EXCHANGE, event);
    }

    @Transactional
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

    @Transactional
    public FollowFromInsight addFollowFromInsight(FollowFromInsightCreateDto dto) {
        insightQueryDomainService.validateWriter(dto.getFolloweeId(), dto.getInsightId());
        FollowFromInsightId id = FollowFromInsightId.of(dto.getFollowerId(), dto.getFolloweeId(), dto.getInsightId());
        if(followFromInsightRepository.existsById(id)) {
            log.info("[PCDS::addFollowFromInsight] Follow history already exists - followerId({}), followeeId({}), insightId({})",
                    dto.getFollowerId(), dto.getFolloweeId(), dto.getInsightId());
            throw new KeeweException(KeeweRtnConsts.ERR428);
        }

        FollowFromInsight followFromInsight = FollowFromInsight.of(
                userDomainService.getUserByIdOrElseThrow(dto.getFollowerId()),
                userDomainService.getUserByIdOrElseThrow(dto.getFolloweeId()),
                insightQueryDomainService.getByIdOrElseThrow(dto.getInsightId())
        );
        return followFromInsightRepository.save(followFromInsight);
    }
}
