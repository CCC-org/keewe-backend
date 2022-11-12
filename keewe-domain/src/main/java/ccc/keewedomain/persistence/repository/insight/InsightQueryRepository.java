package ccc.keewedomain.persistence.repository.insight;

import ccc.keewedomain.persistence.domain.challenge.ChallengeParticipation;
import ccc.keewedomain.persistence.domain.insight.Insight;
import ccc.keewedomain.persistence.domain.user.QUser;
import ccc.keewedomain.persistence.domain.user.User;
import ccc.keewedomain.persistence.repository.utils.CursorPageable;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static ccc.keewedomain.persistence.domain.challenge.QChallenge.challenge;
import static ccc.keewedomain.persistence.domain.challenge.QChallengeParticipation.challengeParticipation;
import static ccc.keewedomain.persistence.domain.common.QInterest.interest;
import static ccc.keewedomain.persistence.domain.insight.QInsight.insight;
import static ccc.keewedomain.persistence.domain.user.QFollow.follow;
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

    public Long countValidByParticipation(ChallengeParticipation participation) {
        return queryFactory.select(insight.id.count())
                .from(insight)
                .where(insight.challengeParticipation.id.eq(participation.getId())
                        .and(insight.deleted.isFalse())
                        .and(insight.valid.isTrue()))
                .fetchFirst();
    }

    public List<Insight> findForHome(User user, CursorPageable<Long> cPage, Boolean follow) {
        BooleanExpression followFilter = Expressions.asBoolean(true).isTrue();
        if (follow != null && follow)
            followFilter = insight.writer.id.in(findFolloweesId(user));

        return queryFactory.select(insight)
                .from(insight)
                .where(followFilter
                        .and(insight.id.lt(cPage.getCursor()))
                        .and(insight.writer.ne(user))
                )
                .innerJoin(insight.writer, QUser.user)
                .fetchJoin()
                .orderBy(insight.id.desc())
                .limit(cPage.getLimit())
                .fetch();
    }

    private JPQLQuery<Long> findFolloweesId(User user) {
        return JPAExpressions
                .select(follow.followee.id)
                .from(follow)
                .where(follow.follower.id.eq(user.getId()));
    }

    public Optional<Insight> findByIdWithParticipationAndChallenge(Long insightId) {
        return Optional.ofNullable(queryFactory
                .select(insight)
                .from(insight)
                .leftJoin(insight.challengeParticipation, challengeParticipation)
                .fetchJoin()
                .leftJoin(challengeParticipation.challenge, challenge)
                .fetchJoin()
                .where(insight.id.eq(insightId))
                .fetchOne());
    }

    // countByValidTrueAndIdBefore
    public Long countValidByIdBeforeAndParticipation(ChallengeParticipation participation, Long insightId) {
        return queryFactory
                .select(insight.count())
                .from(insight)
                .where(insight.challengeParticipation.eq(participation)
                        .and(insight.valid.isTrue())
                        .and(insight.deleted.isFalse())
                        .and(insight.id.lt(insightId)))
                .fetchFirst();
    }
}
