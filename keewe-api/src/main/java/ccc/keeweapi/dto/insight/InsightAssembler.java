package ccc.keeweapi.dto.insight;

import ccc.keeweapi.utils.SecurityUtil;
import ccc.keewedomain.domain.insight.Drawer;
import ccc.keewedomain.domain.insight.Insight;
import ccc.keewedomain.dto.insight.DrawerCreateDto;
import ccc.keewedomain.dto.insight.InsightCreateDto;
import org.springframework.stereotype.Component;

@Component
public class InsightAssembler {

    public InsightCreateDto toInsightCreateDto(InsightCreateRequest request) {
        return InsightCreateDto.of(SecurityUtil.getUserId(),
                request.getContents(),
                request.getLink(),
                request.isParticipation(),
                request.getDrawerId());
    }

    public InsightCreateResponse toInsightCreateResponse(Insight insight) {
        return InsightCreateResponse.of(insight.getId());
    }

    public DrawerCreateDto toDrawerCreateDto(DrawerCreateRequest request) {
        return DrawerCreateDto.of(SecurityUtil.getUserId(), request.getName());
    }

    public DrawerCreateResponse toDrawerCreateResponse(Drawer drawer) {
        return DrawerCreateResponse.of(drawer.getId());
    }
}
