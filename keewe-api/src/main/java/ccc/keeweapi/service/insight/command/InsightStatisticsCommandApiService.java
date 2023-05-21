package ccc.keeweapi.service.insight.command;

import ccc.keeweapi.utils.SecurityUtil;
import ccc.keewedomain.service.insight.command.InsightStatisticsCommandDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InsightStatisticsCommandApiService {
    private final InsightStatisticsCommandDomainService insightStatisticsCommandDomainService;

    public void publishProfileVisitFromInsightEvent(Long insightId) {
        Long userId = SecurityUtil.getUserId();
        insightStatisticsCommandDomainService.publishProfileVisitFromInsightEvent(userId, insightId);
    }
}
