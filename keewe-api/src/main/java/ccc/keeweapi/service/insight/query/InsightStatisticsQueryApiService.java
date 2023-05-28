package ccc.keeweapi.service.insight.query;

import ccc.keeweapi.component.ProfileAssembler;
import ccc.keeweapi.dto.insight.response.ProfileVisitFromInsightCountResponse;
import ccc.keeweapi.dto.user.FollowFromInsightCountResponse;
import ccc.keewedomain.service.insight.query.InsightStatisticsQueryDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InsightStatisticsQueryApiService {
    private final InsightStatisticsQueryDomainService insightStatisticsQueryDomainService;
    private final ProfileAssembler profileAssembler;

    public FollowFromInsightCountResponse countFollowFromInsight(Long insightId) {
        Long followFromInsightCount = insightStatisticsQueryDomainService.countFollowFromInsightByInsightId(insightId);
        return profileAssembler.toFollowFromInsightCountResponse(followFromInsightCount);
    }

    public ProfileVisitFromInsightCountResponse countProfileVisitFromInsight(Long insightId) {
        Long profileVisitFromInsightCount = insightStatisticsQueryDomainService.countByInsightId(insightId);
        return profileAssembler.toProfileVisitFromInsightCountResponse(profileVisitFromInsightCount);
    }
}
