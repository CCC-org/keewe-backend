package ccc.keeweapi.service.insight.query;

import ccc.keeweapi.component.ReactionAssembler;
import ccc.keeweapi.dto.insight.ReactionAggregationResponse;
import ccc.keeweapi.utils.SecurityUtil;
import ccc.keewedomain.dto.insight.ReactionAggregationGetDto;
import ccc.keewedomain.persistence.domain.challenge.ChallengeParticipation;
import ccc.keewedomain.persistence.domain.insight.Insight;
import ccc.keewedomain.persistence.domain.insight.enums.ReactionType;
import ccc.keewedomain.service.challenge.query.ChallengeParticipateQueryDomainService;
import ccc.keewedomain.service.insight.ReactionDomainService;
import ccc.keewedomain.service.insight.query.InsightQueryDomainService;
import java.util.AbstractMap.SimpleEntry;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InsightReactionQueryApiService {
    private final ReactionAssembler reactionAssembler;
    private final ReactionDomainService reactionDomainService;
    private final InsightQueryDomainService insightQueryDomainService;
    private final ChallengeParticipateQueryDomainService challengeParticipateQueryDomainService;
    private final ExecutorService executorService;

    public ReactionAggregationResponse aggregateOfCurrentChallenge() {
        ChallengeParticipation participation = challengeParticipateQueryDomainService.getCurrentChallengeParticipation(SecurityUtil.getUser());
        List<Insight> insights = insightQueryDomainService.getRecordedInsights(participation);
        List<CompletableFuture<ReactionAggregationGetDto>> cFutures = insights.stream()
                .map(insight -> CompletableFuture.supplyAsync(() -> reactionDomainService.getCurrentReactionAggregation(insight.getId()), executorService))
                .collect(Collectors.toList());

        Map<ReactionType, Long> reactionCntMap = cFutures.stream()
                .map(cFuture -> cFuture.join())
                .map(reactionAggregate -> Arrays.stream(ReactionType.values())
                        .map(reactionType -> new SimpleEntry(reactionType, reactionAggregate.getByType(reactionType)))
                        .collect(Collectors.toList())
                )
                .flatMap(List::stream)
                .collect(Collectors.toMap(Entry<ReactionType, Long>::getKey, Entry<ReactionType, Long>::getValue, Long::sum));

        return reactionAssembler.toReactionAggregationResponse(reactionCntMap);
    }
}
