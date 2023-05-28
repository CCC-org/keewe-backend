package ccc.keewedomain.persistence.repository.insight;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import static ccc.keewedomain.persistence.domain.user.QFollowFromInsight.followFromInsight;

@Repository
@RequiredArgsConstructor
public class FollowFromInsightQueryRepository {
    private final JPAQueryFactory queryFactory;

    public Long countByInsightId(Long insightId) {
        return queryFactory.select(followFromInsight.count())
                .from(followFromInsight)
                .where(followFromInsight.insight.id.eq(insightId))
                .fetchFirst();
    }
}
