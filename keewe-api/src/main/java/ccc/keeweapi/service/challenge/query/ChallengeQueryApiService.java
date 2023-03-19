package ccc.keeweapi.service.challenge.query;

import ccc.keeweapi.component.ChallengeAssembler;
import ccc.keeweapi.dto.challenge.ChallengeStatisticsResponse;
import ccc.keeweapi.dto.challenge.OpenedChallengeResponse;
import ccc.keeweapi.utils.SecurityUtil;
import ccc.keewedomain.dto.insight.ReactionAggregationGetDto;
import ccc.keewedomain.persistence.domain.challenge.Challenge;
import ccc.keewedomain.persistence.domain.challenge.ChallengeParticipation;
import ccc.keewedomain.persistence.domain.insight.Insight;
import ccc.keewedomain.persistence.repository.utils.CursorPageable;
import ccc.keewedomain.service.challenge.query.ChallengeParticipateQueryDomainService;
import ccc.keewedomain.service.challenge.query.ChallengeQueryDomainService;
import ccc.keewedomain.service.insight.ReactionDomainService;
import ccc.keewedomain.service.insight.query.BookmarkQueryDomainService;
import ccc.keewedomain.service.insight.query.InsightQueryDomainService;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ChallengeQueryApiService {

    private final ChallengeAssembler challengeAssembler;
    private final ChallengeQueryDomainService challengeQueryDomainService;
    private final InsightQueryDomainService insightQueryDomainService;
    private final ChallengeParticipateQueryDomainService challengeParticipateQueryDomainService;
    private final ReactionDomainService reactionDomainService;
    private final BookmarkQueryDomainService bookmarkQueryDomainService;
    private final ExecutorService executorService;

    @Transactional(readOnly = true)
    public List<OpenedChallengeResponse> paginate(CursorPageable<Long> cPage) {
        List<Challenge> challenges = challengeQueryDomainService.paginate(cPage);
        Map<Long, Long> insightCountPerChallengeMap = insightQueryDomainService.getInsightCountPerChallenge(challenges);
        return challenges.stream()
                .map(challenge -> challengeAssembler.toOpenedChallengeResponse(challenge, insightCountPerChallengeMap.getOrDefault(challenge.getId(), 0L)))
                .collect(Collectors.toList());
    }

    public ChallengeStatisticsResponse aggregateChallengeStatistics() {
        ChallengeParticipation participation = challengeParticipateQueryDomainService.getCurrentChallengeParticipation(SecurityUtil.getUser());
        List<Insight> insights = insightQueryDomainService.getRecordedInsights(participation);

        List<CompletableFuture<ReactionAggregationGetDto>> cFutures = insights.stream()
                .map(insight -> CompletableFuture.supplyAsync(() -> reactionDomainService.getCurrentReactionAggregation(insight.getId()), executorService))
                .collect(Collectors.toList());

        long commentCounts = insights.stream()
                .map(insight -> insight.getComments())
                .flatMap(List::stream)
                .count();

        long viewCounts = insights.stream()
                .map(insight -> insight.getView())
                .count();

        Long bookmarkCounts = bookmarkQueryDomainService.countBookmark(insights);

        // blocking 줄이기 위해 마지막에 실행
        long reactionCounts = cFutures.stream()
                .map(CompletableFuture::join)
                .map(ReactionAggregationGetDto::getAllReactionCount)
                .count();

        return challengeAssembler.toChallengeStatisticsResponse(viewCounts, reactionCounts, commentCounts, bookmarkCounts, 0L);
    }
}
