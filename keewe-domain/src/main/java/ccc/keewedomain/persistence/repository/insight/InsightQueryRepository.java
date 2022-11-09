package ccc.keewedomain.persistence.repository.insight;

import ccc.keewedomain.persistence.domain.challenge.ChallengeParticipation;
import ccc.keewedomain.persistence.domain.insight.Insight;
import ccc.keewedomain.persistence.domain.user.QUser;
import ccc.keewedomain.persistence.domain.user.User;
import ccc.keewedomain.persistence.repository.utils.CursorPageable;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

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

    public Long countValidForParticipation(ChallengeParticipation participation) {
        return queryFactory.select(insight.id.count())
                .from(insight)
                .where(insight.challengeParticipation.id.eq(participation.getId())
                        .and(insight.deleted.isFalse())
                        .and(insight.valid.isTrue()))
                .fetchFirst();
    }

    public List<Insight> findForHome(User user, CursorPageable<Long> cPage) {
        return queryFactory.select(insight)
                .from(insight)
                .where(insight.writer.in(findFollowees(user))
                        .and(insight.id.lt(cPage.getCursor()))
                )
                .innerJoin(insight.writer, QUser.user)
                .fetchJoin()
                .orderBy(insight.id.desc())
                .limit(cPage.getLimit())
                .fetch();
    }

    private JPQLQuery<User> findFollowees(User user) {
        return JPAExpressions
                .select(follow.followee)
                .from(QUser.user)
                .innerJoin(QUser.user.followers, follow)
                .where(follow.follower.eq(user));
    }
}
