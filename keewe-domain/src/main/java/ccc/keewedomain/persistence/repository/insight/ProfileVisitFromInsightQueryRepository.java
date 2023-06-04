package ccc.keewedomain.persistence.repository.insight;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import static ccc.keewedomain.persistence.domain.insight.QProfileVisitFromInsight.profileVisitFromInsight;

@Repository
@RequiredArgsConstructor
public class ProfileVisitFromInsightQueryRepository {
    private final JPAQueryFactory queryFactory;

    public Long countByInsightId(Long insightId) {
        return queryFactory.select(profileVisitFromInsight.count())
                .from(profileVisitFromInsight)
                .where(profileVisitFromInsight.insight.id.eq(insightId))
                .fetchFirst();
    }
}
