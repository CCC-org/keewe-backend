package ccc.keewedomain.service.insight;

import ccc.keewecore.consts.KeeweConsts;
import ccc.keewecore.consts.KeeweRtnConsts;
import ccc.keewecore.exception.KeeweException;
import ccc.keewedomain.cache.domain.insight.CReactionCount;
import ccc.keewedomain.cache.repository.insight.CReactionCountRepository;
import ccc.keewedomain.persistence.domain.insight.ReactionAggregation;
import ccc.keewedomain.dto.insight.ReactionAggregationGetDto;
import ccc.keewedomain.dto.insight.ReactionDto;
import ccc.keewedomain.dto.insight.ReactionIncrementDto;
import ccc.keewedomain.persistence.domain.insight.Insight;
import ccc.keewedomain.persistence.domain.insight.Reaction;
import ccc.keewedomain.persistence.domain.insight.enums.ReactionType;
import ccc.keewedomain.persistence.domain.insight.id.ReactionAggregationId;
import ccc.keewedomain.persistence.domain.notification.Notification;
import ccc.keewedomain.persistence.domain.notification.enums.NotificationContents;
import ccc.keewedomain.persistence.domain.user.User;
import ccc.keewedomain.persistence.repository.insight.ReactionAggregationRepository;
import ccc.keewedomain.persistence.repository.insight.ReactionQueryRepository;
import ccc.keewedomain.persistence.repository.insight.ReactionRepository;
import ccc.keewedomain.service.insight.query.InsightQueryDomainService;
import ccc.keewedomain.service.notification.command.NotificationCommandDomainService;
import ccc.keewedomain.service.user.UserDomainService;
import ccc.keeweinfra.service.messagequeue.MQPublishService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReactionDomainService {
    private final MQPublishService mqPublishService;
    private final CReactionCountRepository cReactionCountRepository;
    private final ReactionAggregationRepository reactionAggregationRepository;
    private final ReactionRepository reactionRepository;
    private final ReactionQueryRepository reactionQueryRepository;
    private final UserDomainService userDomainService;
    private final InsightQueryDomainService insightQueryDomainService;
    private final NotificationCommandDomainService notificationCommandDomainService;

    public Reaction getByIdOrElseThrow(Long id) {
        return reactionRepository.findById(id).orElseThrow(() -> new KeeweException(KeeweRtnConsts.ERR482));
    }

    public ReactionDto react(ReactionIncrementDto dto) {
        ReactionAggregationGetDto reactionAggregation = getCurrentReactionAggregation(dto.getInsightId());

        log.info("[RDS::react] React message pub. id={}", dto.getInsightId());
        mqPublishService.publish(KeeweConsts.INSIGHT_REACT_EXCHANGE, dto);
        ReactionType type = dto.getReactionType();
        return ReactionDto.of(dto.getInsightId(), type, reactionAggregation.getByType(type) + 1L);
    }

    @Transactional
    public Long applyReact(ReactionIncrementDto dto) {
        Long insightId = dto.getInsightId();
        Insight insight = insightQueryDomainService.getByIdOrElseThrow(insightId);
        User user = userDomainService.getUserByIdOrElseThrow(dto.getUserId());
        ReactionAggregation reactionAggregation = getReactionAggregationByIdWithLock(new ReactionAggregationId(insightId, dto.getReactionType()));
        ReactionAggregationGetDto reactionCnt = reactionAggregationRepository.findDtoByInsightId(insightId);
        reactionAggregation.incrementCountByValue(dto.getValue());
        Reaction reaction = reactionRepository.save(Reaction.of(insight, user, dto.getReactionType()));
        cReactionCountRepository.save(createCReactionCountForUpdate(insightId, reactionAggregation, reactionCnt));
        Long reactionCount = reactionQueryRepository.countsAllByInsightAndReactor(insight, user);
        if (reactionCount <= 1) {
            afterReaction(insight, reaction);
        }
        log.info("[RDS::applyReact] insightId({}), count({})", insight.getId(), reactionAggregation.getCount());
        return reactionAggregation.getCount();
    }

    public ReactionAggregation getReactionAggregationByIdWithLock(ReactionAggregationId reactionAggregationId) {
        return reactionAggregationRepository.findByIdWithReadWriteLock(reactionAggregationId)
                .orElseThrow(() -> new KeeweException(KeeweRtnConsts.ERR471));
    }

    public ReactionAggregationGetDto getCurrentReactionAggregation(Long insightId) {
        return ReactionAggregationGetDto.createByCnt(cReactionCountRepository.findByIdWithMissHandle(insightId, () ->
                reactionAggregationRepository.findDtoByInsightId(insightId)
        ));
    }

    // TODO : 리팩토링...
    private CReactionCount createCReactionCountForUpdate(Long insightId, ReactionAggregation reactionAggregation, ReactionAggregationGetDto reactionCnt) {
        CReactionCount cnt = CReactionCount.of(
                insightId,
                reactionCnt.getClap(),
                reactionCnt.getHeart(),
                reactionCnt.getSad(),
                reactionCnt.getSurprise(),
                reactionCnt.getFire(),
                reactionCnt.getEyes()
        );
        cnt.setByType(reactionAggregation.getType(), reactionAggregation.getCount());

        return cnt;
    }

    private void afterReaction(Insight insight, Reaction reaction) {
        try {
            Notification notification = Notification.of(insight.getWriter(), NotificationContents.반응, String.valueOf(reaction.getId()));
            notificationCommandDomainService.save(notification);
        } catch (Throwable t) {
            log.warn("[RDS::afterReaction] 팔로우 후 작업 실패 - reactorId({}), insightId({})", reaction.getReactor().getId(), insight.getId(), t);
        }
    }
}
