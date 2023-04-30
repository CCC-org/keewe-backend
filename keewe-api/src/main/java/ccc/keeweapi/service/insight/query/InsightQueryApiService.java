package ccc.keeweapi.service.insight.query;

import ccc.keeweapi.component.InsightAssembler;
import ccc.keeweapi.component.ProfileAssembler;
import ccc.keeweapi.dto.insight.ChallengeRecordResponse;
import ccc.keeweapi.dto.insight.InsightAuthorAreaResponse;
import ccc.keeweapi.dto.insight.InsightGetForHomeResponse;
import ccc.keeweapi.dto.insight.InsightGetResponse;
import ccc.keeweapi.dto.insight.InsightMyPageResponse;
import ccc.keeweapi.dto.insight.InsightStatisticsResponse;
import ccc.keeweapi.utils.BlockFilterUtil;
import ccc.keeweapi.utils.SecurityUtil;
import ccc.keewecore.consts.KeeweRtnConsts;
import ccc.keewecore.exception.KeeweException;
import ccc.keewedomain.dto.insight.InsightGetDto;
import ccc.keewedomain.persistence.domain.challenge.Challenge;
import ccc.keewedomain.persistence.domain.challenge.ChallengeParticipation;
import ccc.keewedomain.persistence.domain.insight.Insight;
import ccc.keewedomain.persistence.domain.user.User;
import ccc.keewedomain.persistence.repository.utils.CursorPageable;
import ccc.keewedomain.service.challenge.query.ChallengeParticipateQueryDomainService;
import ccc.keewedomain.service.insight.CommentDomainService;
import ccc.keewedomain.service.insight.ReactionDomainService;
import ccc.keewedomain.service.insight.query.BookmarkQueryDomainService;
import ccc.keewedomain.service.insight.query.InsightQueryDomainService;
import ccc.keewedomain.service.user.query.ProfileQueryDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InsightQueryApiService {

    private final InsightQueryDomainService insightQueryDomainService;
    private final ReactionDomainService reactionDomainService;
    private final CommentDomainService commentDomainService;
    private final BookmarkQueryDomainService bookmarkQueryDomainService;
    private final ProfileQueryDomainService profileQueryDomainService;
    private final InsightAssembler insightAssembler;
    private final ProfileAssembler profileAssembler;
    private final ChallengeParticipateQueryDomainService challengeParticipateQueryDomainService;
    private final BlockFilterUtil blockFilterUtil;

    @Transactional(readOnly = true)
    public InsightGetResponse getInsight(Long insightId) {
        blockFilterUtil.filterInsightWriter(insightId);
        InsightGetDto insightGetDto = insightQueryDomainService.getInsight(insightAssembler.toInsightDetailDto(insightId));
        return insightAssembler.toInsightGetResponse(insightGetDto);
    }

    @Transactional(readOnly = true)
    public InsightAuthorAreaResponse getInsightAuthorAreaInfo(Long insightId) {
        blockFilterUtil.filterInsightWriter(insightId);
        Insight insight = insightQueryDomainService.getByIdWithWriter(insightId);
        boolean isFollowing = profileQueryDomainService.isFollowing(profileAssembler.toFollowCheckDto(insight.getWriter().getId()));
        return insightAssembler.toInsightAuthorAreaResponse(insight, isFollowing);
    }


    public List<InsightGetForHomeResponse> getInsightsForHome(CursorPageable<Long> cPage, Boolean follow) {
        List<InsightGetForHomeResponse> responses = insightQueryDomainService.getInsightsForHome(SecurityUtil.getUser(), cPage, follow).stream()
                .map(insightAssembler::toInsightGetForHomeResponse)
                .collect(Collectors.toList());
        return blockFilterUtil.filterUserInResponse(responses);
    }

    // FIXME DTO 수정할 때 같이 네이밍 수정 필요
    @Transactional(readOnly = true)
    public List<InsightGetForHomeResponse> paginateInsightsOfChallenge(CursorPageable<Long> cPage, Long writerId) {
        User user = SecurityUtil.getUser();
        Challenge challenge = challengeParticipateQueryDomainService.findCurrentParticipationByUserId(user.getId())
                .map(ChallengeParticipation::getChallenge)
                .orElseThrow(() -> new KeeweException(KeeweRtnConsts.ERR432));

        List<InsightGetForHomeResponse> responses = insightQueryDomainService.getByChallenge(challenge, user, cPage, writerId).stream()
                .map(insightAssembler::toInsightGetForHomeResponse)
                .collect(Collectors.toList());
        return blockFilterUtil.filterUserInResponse(responses);
    }

    @Transactional(readOnly = true)
    public List<InsightGetForHomeResponse> getInsightForBookmark(CursorPageable<Long> cPage) {
        List<InsightGetForHomeResponse> responses = insightQueryDomainService.getInsightForBookmark(SecurityUtil.getUser(), cPage).stream()
                .map(insightAssembler::toInsightGetForHomeResponse)
                .collect(Collectors.toList());
        return blockFilterUtil.filterUserInResponse(responses);
    }

    public ChallengeRecordResponse getChallengeRecord(Long insightId) {
        blockFilterUtil.filterInsightWriter(insightId);
        Insight insight = insightQueryDomainService.getByIdWithChallengeOrElseThrow(insightId);
        ChallengeParticipation participation = insight.getChallengeParticipation();

        if (Objects.isNull(participation)) {
            return null;
        }

        if (insight.isValid()) {
            Long order = insightQueryDomainService.getRecordOrder(participation, insightId);
            return insightAssembler.toChallengeRecordResponse(participation, order);
        }

        return insightAssembler.toChallengeRecordResponse(participation.getChallenge());
    }

    @Transactional(readOnly = true)
    public List<InsightMyPageResponse> getInsightsForMyPage(Long userId, Long drawerId, CursorPageable<Long> cPage) {
        blockFilterUtil.filterUserId(userId);
        return insightQueryDomainService.getInsightsForMyPage(SecurityUtil.getUser(), userId, drawerId, cPage).stream()
                .map(insightAssembler::toInsightMyPageResponse)
                .collect(Collectors.toList());
    }

    public InsightStatisticsResponse getStatistics(Long insightId) {
        insightQueryDomainService.validateWriter(SecurityUtil.getUserId(), insightId);
        Long viewCount = insightQueryDomainService.getViewCount(insightId);
        Long reactionCount = reactionDomainService.getCurrentReactionAggregation(insightId).getAllReactionCount();
        Long commentCount = commentDomainService.countByInsightId(insightId);
        Long bookmarkCount = bookmarkQueryDomainService.countBookmarkByInsightId(insightId);
        Long shareCount = 0L; // FIXME 공유 카운팅 추가 시 변경 필요
        return insightAssembler.toInsightStatisticsResponse(viewCount, reactionCount, commentCount,bookmarkCount, shareCount);
    }
}
