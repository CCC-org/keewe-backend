package ccc.keewedomain.service.insight.query;

import ccc.keewedomain.persistence.repository.insight.FollowFromInsightQueryRepository;
import ccc.keewedomain.persistence.repository.insight.ProfileVisitFromInsightQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InsightStatisticsQueryDomainService {
    private final ProfileVisitFromInsightQueryRepository profileVisitFromInsightQueryRepository;
    private final FollowFromInsightQueryRepository followFromInsightQueryRepository;

    public Long countByInsightId(Long insightId) {
        return profileVisitFromInsightQueryRepository.countByInsightId(insightId);
    }

    public Long countFollowFromInsightByInsightId(Long insightId) {
        return followFromInsightQueryRepository.countByInsightId(insightId);
    }
}
