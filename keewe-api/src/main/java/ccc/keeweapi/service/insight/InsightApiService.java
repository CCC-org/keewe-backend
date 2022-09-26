package ccc.keeweapi.service.insight;

import ccc.keeweapi.component.InsightAssembler;
import ccc.keeweapi.dto.insight.InsightCreateRequest;
import ccc.keeweapi.dto.insight.InsightCreateResponse;
import ccc.keewedomain.domain.insight.Insight;
import ccc.keewedomain.service.insight.InsightDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InsightApiService {

    private final InsightDomainService insightDomainService;
    private final InsightAssembler insightAssembler;

    public InsightCreateResponse create(InsightCreateRequest request) {
        Insight insight = insightDomainService.create(insightAssembler.toInsightCreateDto(request));
        return insightAssembler.toInsightCreateResponse(insight);
    }

    public void incrementViewCount(Long insightId) {
        insightDomainService.incrementViewCount(insightAssembler.toInsightViewIncrementDto(insightId));
    }
}
