package ccc.keewedomain.service.insight;

import ccc.keewecore.consts.KeeweConsts;
import ccc.keewecore.consts.KeeweRtnConsts;
import ccc.keewecore.exception.KeeweException;
import ccc.keewedomain.cache.domain.insight.CReactionCount;
import ccc.keewedomain.cache.domain.insight.id.CReactionCountId;
import ccc.keewedomain.cache.repository.insight.CReactionCountRepository;
import ccc.keewedomain.domain.insight.ReactionAggregation;
import ccc.keewedomain.dto.insight.ReactionDto;
import ccc.keewedomain.dto.insight.ReactionIncrementDto;
import ccc.keewedomain.persistence.domain.insight.Insight;
import ccc.keewedomain.persistence.domain.insight.Reaction;
import ccc.keewedomain.persistence.domain.insight.id.ReactionAggregationId;
import ccc.keewedomain.persistence.domain.user.User;
import ccc.keewedomain.persistence.repository.insight.ReactionAggregationRepository;
import ccc.keewedomain.persistence.repository.insight.ReactionRepository;
import ccc.keewedomain.service.user.UserDomainService;
import ccc.keeweinfra.service.MQPublishService;
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
    private final UserDomainService userDomainService;
    private final InsightDomainService insightDomainService;

    public ReactionDto react(ReactionIncrementDto dto) {
        CReactionCountId id = new CReactionCountId(dto.getInsightId(), dto.getReactionType()); // TODO : 통일된(효율적인) Key 생성
        Long reactionCount = getCurrentReactionCount(id) + dto.getValue();

        log.info("[RDS::react] React message pub. id={}", id);
        mqPublishService.publish(KeeweConsts.INSIGHT_REACT_EXCHANGE, dto);
        return ReactionDto.of(dto.getInsightId(), dto.getReactionType(), reactionCount);
    }

    @Transactional
    public Long applyReact(ReactionIncrementDto dto) {
        Insight insight = insightDomainService.getByIdOrElseThrow(dto.getInsightId());
        User user = userDomainService.getUserByIdOrElseThrow(dto.getUserId());
        ReactionAggregation reactionAggregation = getReactionAggregationByIdWithLock(new ReactionAggregationId(insight.getId(), dto.getReactionType()));

        reactionAggregation.incrementCountByValue(dto.getValue());
        reactionRepository.save(Reaction.of(insight, user, dto.getReactionType()));
        cReactionCountRepository.save(CReactionCount.of(
                new CReactionCountId(dto.getInsightId(), dto.getReactionType()).toString(),
                reactionAggregation.getCount()
        ));
        log.info("[RDS::applyReact] count {}", reactionAggregation.getCount());

        return reactionAggregation.getCount();
    }

    public ReactionAggregation getReactionAggregationByIdWithLock(ReactionAggregationId reactionAggregationId) {
        return reactionAggregationRepository.findByIdWithReadWriteLock(reactionAggregationId)
                .orElseThrow(() -> new KeeweException(KeeweRtnConsts.ERR471));
    }

    private Long getCurrentReactionCount(CReactionCountId id) {
        CReactionCount cReactionCount = cReactionCountRepository.findByIdWithMissHandle(id, () ->
            reactionAggregationRepository.findByIdOrElseThrow(new ReactionAggregationId(id.getInsightId(), id.getReactionType()))
        );
        return cReactionCount.getCount();
    }
}
