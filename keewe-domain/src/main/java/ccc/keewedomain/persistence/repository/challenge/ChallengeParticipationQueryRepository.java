package ccc.keewedomain.persistence.repository.challenge;

import ccc.keewedomain.persistence.domain.challenge.ChallengeParticipation;
import ccc.keewedomain.persistence.domain.challenge.enums.ChallengeParticipationStatus;
import com.querydsl.core.group.GroupBy;
import com.querydsl.core.types.dsl.DateTemplate;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

import static ccc.keewedomain.persistence.domain.challenge.QChallenge.challenge;
import static ccc.keewedomain.persistence.domain.challenge.QChallengeParticipation.challengeParticipation;
import static ccc.keewedomain.persistence.domain.insight.QInsight.insight;
import static ccc.keewedomain.persistence.domain.user.QUser.user;

@Repository
@RequiredArgsConstructor
public class ChallengeParticipationQueryRepository {

    private final JPAQueryFactory queryFactory;

    private final DateTemplate<String> insightCreatedDate = Expressions.dateTemplate(
            String.class,
            "DATE_FORMAT({0}, {1})",
            insight.createdAt,
            "%Y-%m-%d");

    public boolean existsByChallengerIdAndStatus(Long challengerId, ChallengeParticipationStatus status) {
        Integer fetchFirst = queryFactory
                .selectOne()
                .from(challengeParticipation)
                .where(user.id.eq(challengerId).and(challengeParticipation.status.eq(status)))
                .fetchFirst();

        return fetchFirst != null;
    }

    public Optional<ChallengeParticipation> findByChallengerIdAndStatusWithChallenge(Long challengerId, ChallengeParticipationStatus status) {
        return Optional.ofNullable(queryFactory
                .select(challengeParticipation)
                .from(challengeParticipation)
                .innerJoin(challengeParticipation.challenge, challenge)
                .fetchJoin()
                .where(challengeParticipation.challenger.id.eq(challengerId).and(challengeParticipation.status.eq(status)))
                .fetchOne());
    }

    public Map<String, Long> getRecordCountPerDate(ChallengeParticipation participation, LocalDateTime startDateTime, LocalDateTime endDateTime) {
        return queryFactory
                .from(insight)
                .where(insight.challengeParticipation.eq(participation)
                        .and(insight.valid.isTrue())
                        .and(insight.createdAt.goe(startDateTime).and(insight.createdAt.lt(endDateTime))))
                .groupBy(insightCreatedDate)
                .transform(GroupBy.groupBy(insightCreatedDate).as(insight.count()));
    }
}
