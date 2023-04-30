package ccc.keeweapi.service.insight.command;

import ccc.keeweapi.component.InsightAssembler;
import ccc.keeweapi.dto.insight.request.InsightCreateRequest;
import ccc.keeweapi.dto.insight.request.InsightUpdateRequest;
import ccc.keeweapi.dto.insight.response.BookmarkToggleResponse;
import ccc.keeweapi.dto.insight.response.InsightCreateResponse;
import ccc.keeweapi.dto.insight.response.InsightDeleteResponse;
import ccc.keeweapi.dto.insight.response.InsightUpdateResponse;
import ccc.keeweapi.dto.insight.response.InsightViewIncrementResponse;
import ccc.keeweapi.utils.BlockFilterUtil;
import ccc.keeweapi.utils.SecurityUtil;
import ccc.keeweapi.utils.annotations.TitleEventPublish;
import ccc.keewecore.consts.TitleCategory;
import ccc.keewedomain.persistence.domain.insight.Insight;
import ccc.keewedomain.service.insight.command.BookmarkCommandDomainService;
import ccc.keewedomain.service.insight.command.InsightCommandDomainService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class InsightCommandApiService {

    private final BookmarkCommandDomainService bookmarkCommandDomainService;
    private final InsightCommandDomainService insightCommandDomainService;
    private final InsightAssembler insightAssembler;
    private final BlockFilterUtil blockFilterUtil;

    @TitleEventPublish(titleCategory = TitleCategory.INSIGHT)
    public InsightCreateResponse create(InsightCreateRequest request) {
        Insight insight = insightCommandDomainService.create(insightAssembler.toInsightCreateDto(request));
        log.info("[ICAS::create] 인사이트 생성 완료 - insightId({}), requestUserId({})", insight.getId(), SecurityUtil.getUserId());
        return insightAssembler.toInsightCreateResponse(insight);
    }

    public InsightUpdateResponse update(InsightUpdateRequest request) {
        Insight updatedInsight = insightCommandDomainService.update(insightAssembler.toInsightUpdateDto(request));
        log.info("[ICAS::update] 인사이트 업데이트 완료 - insightId({}), requestUserId({})", updatedInsight.getId(), SecurityUtil.getUserId());
        return insightAssembler.toInsightUpdateResponse(updatedInsight);
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

    public InsightDeleteResponse delete(Long id) {
        Long deletedInsightId = insightCommandDomainService.delete(insightAssembler.toInsightDeleteDto(id));
        log.info("[ICAS::delete] 인사이트 삭제 완료 - insightId({}), requestUserId({})", deletedInsightId, SecurityUtil.getUserId());
        return InsightDeleteResponse.of(deletedInsightId);
    }
}
