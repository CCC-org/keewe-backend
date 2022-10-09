package ccc.keeweapi.service.insight;

import ccc.keeweapi.dto.insight.DrawerCreateRequest;
import ccc.keeweapi.dto.insight.DrawerCreateResponse;
import ccc.keeweapi.component.InsightAssembler;
import ccc.keeweapi.dto.insight.DrawerResponse;
import ccc.keeweapi.utils.SecurityUtil;
import ccc.keewedomain.persistence.domain.insight.Drawer;
import ccc.keewedomain.service.insight.DrawerDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DrawerApiService {

    private final DrawerDomainService drawerDomainService;

    private final InsightAssembler insightAssembler;

    @Transactional
    public DrawerCreateResponse create(DrawerCreateRequest request) {
        Drawer drawer = drawerDomainService.create(insightAssembler.toDrawerCreateDto(request));
        return insightAssembler.toDrawerCreateResponse(drawer);
    }

    @Transactional(readOnly = true)
    public List<DrawerResponse> getMyDrawers() {
        Long userId = SecurityUtil.getUserId();

        return drawerDomainService.findAllByUserId(userId).stream()
                .map(insightAssembler::toDrawerResponse)
                .collect(Collectors.toList());
    }
}
