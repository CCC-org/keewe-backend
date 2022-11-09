package ccc.keeweapi.service.insight;

import ccc.keeweapi.component.InsightAssembler;
import ccc.keeweapi.component.ProfileAssembler;
import ccc.keeweapi.dto.insight.*;
import ccc.keewedomain.dto.insight.InsightGetDto;
import ccc.keewedomain.persistence.domain.challenge.Challenge;
import ccc.keewedomain.persistence.domain.challenge.ChallengeParticipation;
import ccc.keewedomain.persistence.domain.insight.Insight;
import ccc.keewedomain.service.challenge.ChallengeDomainService;
import ccc.keewedomain.service.insight.InsightDomainService;
import ccc.keewedomain.service.user.ProfileDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class InsightApiService {

    private final InsightDomainService insightDomainService;
    private final ProfileDomainService profileDomainService;
    private final ChallengeDomainService challengeDomainService;
    private final InsightAssembler insightAssembler;
    private final ProfileAssembler profileAssembler;


    @Transactional
    public InsightCreateResponse create(InsightCreateRequest request) {
        Insight insight = insightDomainService.create(insightAssembler.toInsightCreateDto(request));
        return insightAssembler.toInsightCreateResponse(insight);
    }

    public InsightViewIncrementResponse incrementViewCount(Long insightId) {
        Long viewCount = insightDomainService.incrementViewCount(insightAssembler.toInsightViewIncrementDto(insightId));
        return insightAssembler.toInsightViewIncrementResponse(viewCount);
    }

    @Transactional(readOnly = true)
    public InsightGetResponse getInsight(Long insightId) {
        InsightGetDto insightGetDto = insightDomainService.getInsight(insightAssembler.toInsightDetailDto(insightId));
        return insightAssembler.toInsightGetResponse(insightGetDto);
    }

    @Transactional(readOnly = true)
    public InsightAuthorAreaResponse getInsightAuthorAreaInfo(Long insightId) {
        Insight insight = insightDomainService.getByIdWithWriter(insightId);
        boolean isFollowing = profileDomainService.isFollowing(profileAssembler.toFollowCheckDto(insight.getWriter().getId()));
        return insightAssembler.toInsightAuthorAreaResponse(insight, isFollowing);
    }

    public BookmarkToggleResponse toggleInsightBookmark(Long insightId) {
        boolean isBookmark = insightDomainService.toggleInsightBookmark(insightAssembler.toBookmarkToggleDto(insightId));
        return insightAssembler.toBookmarkToggleResponse(isBookmark);
    }

    @Transactional(readOnly = true)
    public ChallengeRecordResponse getChallengeRecord(Long insightId) {
        Insight insight = insightDomainService.getByIdWithChallengeOrElseThrow(insightId);
        ChallengeParticipation participation = insight.getChallengeParticipation();

        if (Objects.isNull(participation)) {
            return null;
        }

        if (insight.isValid()) {
            Long order = insightDomainService.getRecordOrder(participation, insightId);
            return insightAssembler.toChallengeRecordResponse(participation, order);
        }

        return insightAssembler.toChallengeRecordResponse(participation.getChallenge());
    }
}
