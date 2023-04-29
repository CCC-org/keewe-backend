package ccc.keeweapi.service.insight.command;

import ccc.keeweapi.aop.annotations.BlockFilter;
import ccc.keeweapi.component.InsightAssembler;
import ccc.keeweapi.dto.insight.*;
import ccc.keeweapi.utils.BlockFilterUtil;
import ccc.keeweapi.utils.annotations.TitleEventPublish;
import ccc.keewecore.consts.TitleCategory;
import ccc.keewedomain.persistence.domain.insight.Insight;
import ccc.keewedomain.service.insight.command.BookmarkCommandDomainService;
import ccc.keewedomain.service.insight.command.InsightCommandDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class InsightCommandApiService {

    private final BookmarkCommandDomainService bookmarkCommandDomainService;
    private final InsightCommandDomainService insightCommandDomainService;
    private final InsightAssembler insightAssembler;
    private final BlockFilterUtil blockFilterUtil;

    @Transactional
    @TitleEventPublish(titleCategory = TitleCategory.INSIGHT)
    public InsightCreateResponse create(InsightCreateRequest request) {
        Insight insight = insightCommandDomainService.create(insightAssembler.toInsightCreateDto(request));
        return insightAssembler.toInsightCreateResponse(insight);
    }

    public InsightViewIncrementResponse incrementViewCount(Long insightId) {
        blockFilterUtil.filterInsightWriter(insightId);
        Long viewCount = insightCommandDomainService.incrementViewCount(insightAssembler.toInsightViewIncrementDto(insightId));
        return insightAssembler.toInsightViewIncrementResponse(viewCount);
    }

    public BookmarkToggleResponse toggleInsightBookmark(Long insightId) {
        boolean isBookmark = bookmarkCommandDomainService.toggleInsightBookmark(insightAssembler.toBookmarkToggleDto(insightId));
        return insightAssembler.toBookmarkToggleResponse(isBookmark);
    }

    @Transactional
    public InsightDeleteResponse delete(Long id) {
        return InsightDeleteResponse.of(insightCommandDomainService.delete(insightAssembler.toInsightDeleteDto(id)));
    }
}
