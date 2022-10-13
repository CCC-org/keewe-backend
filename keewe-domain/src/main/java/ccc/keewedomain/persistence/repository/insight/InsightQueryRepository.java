package ccc.keewedomain.persistence.repository.insight;

import ccc.keewedomain.persistence.domain.challenge.ChallengeParticipation;
import ccc.keewedomain.persistence.domain.insight.Insight;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import static ccc.keewedomain.persistence.domain.common.QInterest.interest;
import static ccc.keewedomain.persistence.domain.insight.QInsight.insight;
import static ccc.keewedomain.persistence.domain.user.QUser.user;

@Repository
@RequiredArgsConstructor
public class InsightQueryRepository {
    private final JPAQueryFactory queryFactory;

    public Insight findByIdWithWriter(Long insightId) {
        return queryFactory.select(insight)
                .from(insight)
                .where(insight.id.eq(insightId))
                .leftJoin(insight.writer, user)
                .fetchJoin()
                .leftJoin(user.interests, interest)
                .fetchJoin()
                .fetchOne();

    }

    public Long countValidForParticipation(ChallengeParticipation participation) {
        return queryFactory.select(insight.id.count())
                .from(insight)
                .where(insight.challengeParticipation.id.eq(participation.getId())
                        .and(insight.deleted.isFalse())
                        .and(insight.valid.isTrue()))
                .fetchFirst();
    }
}
