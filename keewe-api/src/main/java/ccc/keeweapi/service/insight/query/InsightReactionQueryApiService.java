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
import java.util.concurrent.Executor;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.concurrent.ListenableFuture;

@Service
@RequiredArgsConstructor
public class InsightReactionQueryApiService {
    private final ReactionAssembler reactionAssembler;
    private final ReactionDomainService reactionDomainService;
    private final InsightQueryDomainService insightQueryDomainService;
    private final ChallengeParticipateQueryDomainService challengeParticipateQueryDomainService;
    private final Executor worker;

    public ReactionAggregationResponse aggregateOfCurrentChallenge() {
        Assert.isInstanceOf(ThreadPoolTaskExecutor.class, worker);

        ChallengeParticipation participation = challengeParticipateQueryDomainService.getCurrentChallengeParticipation(SecurityUtil.getUser());
        List<Insight> insights = insightQueryDomainService.getRecordedInsights(participation);
        ThreadPoolTaskExecutor threadPoolTaskExecutor = (ThreadPoolTaskExecutor) worker;
        List<ListenableFuture<ReactionAggregationGetDto>> lFutures = insights.stream()
                .map(insight -> threadPoolTaskExecutor.submitListenable(() -> reactionDomainService.getCurrentReactionAggregation(insight.getId())))
                .collect(Collectors.toList());

        Map<ReactionType, Long> reactionCntMap = lFutures.stream()
                .map(lFuture -> lFuture.completable().join())
                .map(reactionAggregate -> Arrays.stream(ReactionType.values())
                        .map(reactionType -> new SimpleEntry(reactionType, reactionAggregate.getByType(reactionType)))
                        .collect(Collectors.toList())
                )
                .flatMap(List::stream)
                .collect(Collectors.toMap(Entry<ReactionType, Long>::getKey, Entry<ReactionType, Long>::getValue, Long::sum));

        return reactionAssembler.toReactionAggregationResponse(reactionCntMap);
    }
}
