package ccc.keeweapi.service.insight.query;

import ccc.keeweapi.aop.annotations.BlockFilter;
import ccc.keeweapi.component.InsightAssembler;
import ccc.keeweapi.dto.insight.DrawerResponse;
import ccc.keeweapi.utils.SecurityUtil;
import ccc.keewedomain.service.insight.DrawerDomainService;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class InsightDrawerQueryApiService {

    private final DrawerDomainService drawerDomainService;
    private final InsightAssembler insightAssembler;

    @Transactional(readOnly = true)
    public List<DrawerResponse> getMyDrawers() {
        Long userId = SecurityUtil.getUserId();

        return drawerDomainService.findAllByUserId(userId).stream()
                .map(insightAssembler::toDrawerResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    @BlockFilter
    public List<DrawerResponse> getDrawers(Long userId) {
        return drawerDomainService.findAllByUserId(userId).stream()
                .map(insightAssembler::toDrawerResponse)
                .collect(Collectors.toList());
    }
}
