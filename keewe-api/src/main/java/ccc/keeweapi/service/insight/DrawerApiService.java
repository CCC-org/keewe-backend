package ccc.keeweapi.service.insight;

import ccc.keeweapi.dto.insight.DrawerCreateRequest;
import ccc.keeweapi.dto.insight.DrawerCreateResponse;
import ccc.keeweapi.component.InsightAssembler;
import ccc.keewedomain.domain.insight.Drawer;
import ccc.keewedomain.service.insight.DrawerDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DrawerApiService {

    private final DrawerDomainService drawerDomainService;

    private final InsightAssembler insightAssembler;

    public DrawerCreateResponse create(DrawerCreateRequest request) {
        Drawer drawer = drawerDomainService.create(insightAssembler.toDrawerCreateDto(request));
        return insightAssembler.toDrawerCreateResponse(drawer);
    }
}
