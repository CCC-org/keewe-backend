package ccc.keeweapi.service.insight.query;

import ccc.keeweapi.component.ProfileAssembler;
import ccc.keeweapi.dto.user.FollowFromInsightCountResponse;
import ccc.keewedomain.service.user.query.ProfileQueryDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InsightStatisticsQueryApiService {
    private final ProfileQueryDomainService profileQueryDomainService;
    private final ProfileAssembler profileAssembler;

    public FollowFromInsightCountResponse countFollowFromInsight(Long insightId) {
        Long followFromInsightCount = profileQueryDomainService.countFollowFromInsightByInsightId(insightId);
        return profileAssembler.toFollowFromInsightCountResponse(followFromInsightCount);
    }
}
