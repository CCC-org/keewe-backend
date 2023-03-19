package ccc.keewedomain.persistence.repository.challenge;

import ccc.keewedomain.persistence.domain.challenge.Challenge;
import ccc.keewedomain.persistence.domain.challenge.ChallengeParticipation;
import ccc.keewedomain.persistence.domain.challenge.enums.ChallengeParticipationStatus;
import ccc.keewedomain.persistence.domain.user.User;
import ccc.keewedomain.persistence.repository.utils.CursorPageable;
import com.querydsl.core.group.GroupBy;
import com.querydsl.core.types.dsl.DateTemplate;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static ccc.keewedomain.persistence.domain.challenge.QChallenge.challenge;
import static ccc.keewedomain.persistence.domain.challenge.QChallengeParticipation.challengeParticipation;
import static ccc.keewedomain.persistence.domain.challenge.enums.ChallengeParticipationStatus.CHALLENGING;
import static ccc.keewedomain.persistence.domain.insight.QInsight.insight;
import static ccc.keewedomain.persistence.domain.user.QFollow.follow;
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
        Integer fetchFirst = queryFactory.selectOne()
                .from(challengeParticipation)
                .where(user.id.eq(challengerId).and(challengeParticipation.status.eq(status)))
                .fetchFirst();

        return fetchFirst != null;
    }

    public Optional<ChallengeParticipation> findByUserIdAndStatus(Long userId, ChallengeParticipationStatus status) {
        return Optional.ofNullable(
                queryFactory.select(challengeParticipation)
                .from(challengeParticipation)
                .innerJoin(challengeParticipation.challenge, challenge)
                .fetchJoin()
                .where(challengeParticipation.challenger.id.eq(userId).and(challengeParticipation.status.eq(status)))
                .fetchOne()
        );
    }

    public Map<String, Long> getRecordCountPerDate(ChallengeParticipation participation, LocalDateTime startDateTime, LocalDateTime endDateTime) {
        return queryFactory.from(insight)
                .where(insight.challengeParticipation.eq(participation)
                        .and(insight.valid.isTrue())
                        .and(insight.createdAt.goe(startDateTime).and(insight.createdAt.lt(endDateTime))))
                .groupBy(insightCreatedDate)
                .transform(GroupBy.groupBy(insightCreatedDate).as(insight.count()));
    }

    public Long countByChallengeAndStatus(Challenge challenge, ChallengeParticipationStatus status) {
        return queryFactory
                .select(challengeParticipation.count())
                .from(challengeParticipation)
                .where(challengeParticipation.challenge.eq(challenge))
                .where(challengeParticipation.status.eq(status))
                .fetchFirst();
    }

    public List<ChallengeParticipation> findFinishedParticipation(User challenger, Long size) {
        return queryFactory.select(challengeParticipation)
                .from(challengeParticipation)
                .innerJoin(challengeParticipation.challenge, challenge)
                .fetchJoin()
                .where(challengeParticipation.challenger.eq(challenger))
                .where(challengeParticipation.status.ne(ChallengeParticipationStatus.CHALLENGING))
                .orderBy(challengeParticipation.id.desc())
                .limit(size)
                .fetch();
    }

    public List<ChallengeParticipation> findByChallengeOrderByFollowing(Challenge targetChallenge, User targetUser, Pageable pageable) {
        return queryFactory.select(challengeParticipation)
                .from(challengeParticipation)
                .innerJoin(challengeParticipation.challenger, user)
                .fetchJoin()
                .leftJoin(user.followees, follow).on(follow.follower.eq(targetUser))
                .where(challengeParticipation.challenge.eq(targetChallenge))
                .where(challengeParticipation.status.eq(CHALLENGING))
                .where(follow.follower.eq(targetUser).or(follow.follower.isNull()))
                .orderBy(follow.follower.id.asc().nullsLast())
                .orderBy(challengeParticipation.createdAt.asc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
    }
    
    public List<ChallengeParticipation> paginateFinished(User challenger, CursorPageable<Long> cPage) {
        return queryFactory.select(challengeParticipation)
                .from(challengeParticipation)
                .innerJoin(challengeParticipation.challenge, challenge)
                .fetchJoin()
                .where(challengeParticipation.challenger.eq(challenger)
                    .and(challengeParticipation.status.ne(ChallengeParticipationStatus.CHALLENGING))
                    .and(challengeParticipation.deleted.isFalse())
                    .and(challengeParticipation.id.lt(cPage.getCursor()))
                )
                .orderBy(challengeParticipation.id.desc())
                .limit(cPage.getLimit())
                .fetch();
    }
}
