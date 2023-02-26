package ccc.keeweapi.service.insight.command;

import ccc.keeweapi.component.InsightAssembler;
import ccc.keeweapi.dto.insight.*;
import ccc.keeweapi.utils.annotations.TitleEventPublish;
import ccc.keewecore.consts.TitleCategory;
import ccc.keewedomain.persistence.domain.insight.Insight;
import ccc.keewedomain.service.insight.InsightDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class InsightCommandApiService {

    private final InsightDomainService insightDomainService;
    private final InsightAssembler insightAssembler;

    @Transactional
    @TitleEventPublish(titleCategory = TitleCategory.INSIGHT)
    public InsightCreateResponse create(InsightCreateRequest request) {
        Insight insight = insightDomainService.create(insightAssembler.toInsightCreateDto(request));
        return insightAssembler.toInsightCreateResponse(insight);
    }

    public InsightViewIncrementResponse incrementViewCount(Long insightId) {
        Long viewCount = insightDomainService.incrementViewCount(insightAssembler.toInsightViewIncrementDto(insightId));
        return insightAssembler.toInsightViewIncrementResponse(viewCount);
    }

    public BookmarkToggleResponse toggleInsightBookmark(Long insightId) {
        boolean isBookmark = insightDomainService.toggleInsightBookmark(insightAssembler.toBookmarkToggleDto(insightId));
        return insightAssembler.toBookmarkToggleResponse(isBookmark);
    }

    @Transactional
    public InsightDeleteResponse delete(Long id) {
        return InsightDeleteResponse.of(insightDomainService.delete(insightAssembler.toInsightDeleteDto(id)));
    }
}
