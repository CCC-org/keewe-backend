package ccc.keewedomain.service.insight;

import ccc.keewecore.consts.KeeweRtnConsts;
import ccc.keewecore.exception.KeeweException;
import ccc.keewedomain.domain.insight.Insight;
import ccc.keewedomain.domain.insight.Reaction;
import ccc.keewedomain.domain.insight.ReactionAggregation;
import ccc.keewedomain.domain.insight.enums.ReactionType;
import ccc.keewedomain.domain.insight.id.ReactionAggregationId;
import ccc.keewedomain.domain.user.User;
import ccc.keewedomain.dto.insight.ReactionDto;
import ccc.keewedomain.repository.insight.InsightRepository;
import ccc.keewedomain.repository.insight.ReactionAggregationRepository;
import ccc.keewedomain.repository.insight.ReactionRepository;
import ccc.keewedomain.service.user.UserDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReactionDomainService {
    private final ReactionRepository reactionRepository;
    private final ReactionAggregationRepository reactionAggregationRepository;
    private final UserDomainService userDomainService;
    private final InsightRepository insightRepository;

    public Reaction react(ReactionDto dto) {
        User user = userDomainService.getUserByIdOrElseThrow(dto.getUserId());
        Insight insight = getInsightByIdOrElseThrow(dto.getInsightId());
        Reaction reaction = Reaction.of(insight, user, dto.getReactionType());

        reactionRepository.save(reaction);
        aggregateReaction(insight, dto.getReactionType(), 1L);
        return reaction;
    }

    private void aggregateReaction(Insight insight, ReactionType reactionType, Long value) {
        Optional<ReactionAggregation> optionalEntity = reactionAggregationRepository
                .findByIdForUpdate(ReactionAggregationId.of(insight.getId(), reactionType));

        if (optionalEntity.isEmpty()) {
            reactionAggregationRepository.save(ReactionAggregation.of(insight, reactionType, value));
        }
        else {
            ReactionAggregation entity = optionalEntity.get();
            entity.incrementCountBy(value);
        }
    }

    private Insight getInsightByIdOrElseThrow(Long insightId) {
        return insightRepository.findById(insightId).orElseThrow(() -> new KeeweException(KeeweRtnConsts.ERR440));
    }
}
