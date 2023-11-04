package ccc.keewedomain.service.search.query;

import ccc.keewecore.consts.SearchType;
import ccc.keewedomain.cache.repository.insight.CReactionCountRepository;
import ccc.keewedomain.dto.SearchDto;
import ccc.keewedomain.dto.challenge.ChallengeSearchDto;
import ccc.keewedomain.dto.insight.InsightGetForSearch;
import ccc.keewedomain.dto.insight.InsightWriterDto;
import ccc.keewedomain.dto.insight.ReactionAggregationGetDto;
import ccc.keewedomain.dto.user.UserSearchDto;
import ccc.keewedomain.persistence.domain.challenge.Challenge;
import ccc.keewedomain.persistence.domain.insight.Insight;
import ccc.keewedomain.persistence.domain.user.User;
import ccc.keewedomain.persistence.repository.insight.InsightQueryRepository;
import ccc.keewedomain.persistence.repository.insight.ReactionAggregationRepository;
import ccc.keewedomain.persistence.repository.utils.CursorPageable;
import ccc.keewedomain.service.insight.query.BookmarkQueryDomainService;
import ccc.keewedomain.service.search.ChallengeSearcher;
import ccc.keewedomain.service.search.InsightSearcher;
import ccc.keewedomain.service.search.UserSearcher;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SearchQueryDomainService {

    private final InsightSearcher insightSearcher;
    private final UserSearcher userSearcher;
    private final ChallengeSearcher challengeSearcher;
    private final BookmarkQueryDomainService bookmarkQueryDomainService;
    private final ReactionAggregationRepository reactionAggregationRepository;
    private final CReactionCountRepository cReactionCountRepository;
    private final InsightQueryRepository insightQueryRepository;

    public List<SearchDto> search(SearchType searchType, String keyword, User user, CursorPageable<Long> cPage) {
        switch (searchType) {
            case INSIGHT:
                List<Insight> insights = insightSearcher.search(keyword, cPage);
                Map<Long, Boolean> bookmarkPresenceMap = bookmarkQueryDomainService.getBookmarkPresenceMap(user, insights);
                return insights.parallelStream().map(i ->
                        InsightGetForSearch.of(
                                i.getId(),
                                i.getContents(),
                                bookmarkPresenceMap.getOrDefault(i.getId(), false),
                                i.getLink(),
                                getCurrentReactionAggregation(i.getId()),
                                i.getCreatedAt(),
                                InsightWriterDto.of(
                                        i.getWriter().getId(),
                                        i.getWriter().getNickname(),
                                        i.getWriter().getRepTitleName(),
                                        i.getWriter().getProfilePhotoURL()
                                )
                        )
                ).collect(Collectors.toList());
            case USER:
                List<User> users = userSearcher.search(keyword, cPage);
                return users.stream().map(u ->
                        UserSearchDto.of(
                                u.getId(),
                                u.getNickname(),
                                u.getProfilePhotoURL(),
                                u.getRepTitleName(),
                                isFollowing(user, u)
                        )
                ).collect(Collectors.toList());
            case CHALLENGE:
                List<Challenge> challenges = challengeSearcher.search(keyword, cPage);
                return challenges.stream().map(c ->
                        ChallengeSearchDto.of(
                                c.getId(),
                                c.getName(),
                                c.getIntroduction(),
                                c.getInterest().getName(),
                                getInsightCntInChallenge(c)
                        )
                ).collect(Collectors.toList());
        }
        return null;
    }

    private ReactionAggregationGetDto getCurrentReactionAggregation(Long insightId) {
        return ReactionAggregationGetDto.createByCnt(cReactionCountRepository.findByIdWithMissHandle(insightId, () ->
                reactionAggregationRepository.findDtoByInsightId(insightId)
        ));
    }

    /*
     * user -> target 으로의 팔로우 여부를 반환한다.
     * TODO : 좀 더 효율적으로 구현할 수 있는 여지가 있을 듯...?
     */
    private boolean isFollowing(User user, User target) {
        return target.getFollowees().stream().anyMatch(f ->
                Objects.equals(f.getFollower().getId(), user.getId())
        );
    }

    private Long getInsightCntInChallenge(Challenge challenge) {
        return insightQueryRepository.countByChallenge(challenge);
    }
}
