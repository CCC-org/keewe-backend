package ccc.keeweapi.dto.insight;

import ccc.keeweapi.utils.SecurityUtil;
import ccc.keewedomain.domain.insight.Insight;
import ccc.keewedomain.dto.insight.InsightCreateDto;
import org.springframework.stereotype.Component;

@Component
public class InsightAssembler {

    public InsightCreateDto toInsightCreateDto(InsightCreateRequest request) {
        return InsightCreateDto.of(SecurityUtil.getUserId(), request.getContents(), request.getLink(), request.getDrawerId());
    }

    public InsightCreateResponse toInsightCreateResponse(Insight insight) {
        return InsightCreateResponse.of(insight.getId());
    }
}
