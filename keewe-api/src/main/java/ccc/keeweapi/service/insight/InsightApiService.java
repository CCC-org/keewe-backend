package ccc.keeweapi.service.insight;

import ccc.keeweapi.component.InsightAssembler;
import ccc.keeweapi.dto.insight.InsightAuthorAreaResponse;
import ccc.keeweapi.dto.insight.InsightCreateRequest;
import ccc.keeweapi.dto.insight.InsightCreateResponse;
import ccc.keeweapi.dto.insight.InsightGetResponse;
import ccc.keeweapi.dto.insight.InsightViewIncrementResponse;
import ccc.keewedomain.dto.insight.InsightGetDto;
import ccc.keewedomain.persistence.domain.insight.Insight;
import ccc.keewedomain.service.insight.InsightDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class InsightApiService {

    private final InsightDomainService insightDomainService;
    private final InsightAssembler insightAssembler;

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
        return insightAssembler.toInsightAuthorAreaResponse(insight);
    }
}
