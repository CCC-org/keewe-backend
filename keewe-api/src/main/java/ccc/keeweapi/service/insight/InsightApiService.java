package ccc.keeweapi.service.insight;

import ccc.keeweapi.component.InsightAssembler;
import ccc.keeweapi.component.ProfileAssembler;
import ccc.keeweapi.dto.insight.*;
import ccc.keeweapi.utils.SecurityUtil;
import ccc.keewedomain.dto.insight.InsightGetDto;
import ccc.keewedomain.persistence.domain.insight.Insight;
import ccc.keewedomain.persistence.repository.utils.CursorPageable;
import ccc.keewedomain.service.insight.InsightDomainService;
import ccc.keewedomain.service.user.ProfileDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InsightApiService {

    private final InsightDomainService insightDomainService;
    private final ProfileDomainService profileDomainService;
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
        InsightGetDto insightGetDto = insightDomainService.getInsight(insightId);
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
    public List<InsightGetResponse> getInsightsForHome(CursorPageable<Long> cPage) {
        List<InsightGetDto> insights = insightDomainService.getInsightsForHome(SecurityUtil.getUser(), cPage);
        return insightDomainService.getInsightsForHome(SecurityUtil.getUser(), cPage).stream()
                .map(insightAssembler::toInsightGetResponse)
                .collect(Collectors.toList());
    }
}
