package ccc.keeweapi.service.insight.command;

import ccc.keeweapi.component.InsightAssembler;
import ccc.keeweapi.dto.insight.DrawerCreateRequest;
import ccc.keeweapi.dto.insight.DrawerCreateResponse;
import ccc.keewedomain.persistence.domain.insight.Drawer;
import ccc.keewedomain.service.insight.DrawerDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class InsightDrawerCommandApiService {

    private final DrawerDomainService drawerDomainService;
    private final InsightAssembler insightAssembler;

    @Transactional
    public DrawerCreateResponse create(DrawerCreateRequest request) {
        Drawer drawer = drawerDomainService.create(insightAssembler.toDrawerCreateDto(request));
        return insightAssembler.toDrawerCreateResponse(drawer);
    }
}
