package ccc.keewedomain.persistence.repository.insight;

import ccc.keewedomain.persistence.domain.challenge.Challenge;
import ccc.keewedomain.persistence.domain.challenge.ChallengeParticipation;
import ccc.keewedomain.persistence.domain.insight.Insight;
import ccc.keewedomain.persistence.domain.user.QUser;
import ccc.keewedomain.persistence.domain.user.User;
import ccc.keewedomain.persistence.repository.utils.CursorPageable;
import com.querydsl.core.Tuple;
import com.querydsl.core.group.GroupBy;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import static ccc.keewedomain.persistence.domain.challenge.QChallenge.challenge;
import static ccc.keewedomain.persistence.domain.challenge.QChallengeParticipation.challengeParticipation;
import static ccc.keewedomain.persistence.domain.common.QInterest.interest;
import static ccc.keewedomain.persistence.domain.insight.QBookmark.bookmark;
import static ccc.keewedomain.persistence.domain.insight.QComment.comment;
import static ccc.keewedomain.persistence.domain.insight.QInsight.insight;
import static ccc.keewedomain.persistence.domain.title.QTitle.title;
import static ccc.keewedomain.persistence.domain.user.QFollow.follow;
import static ccc.keewedomain.persistence.domain.user.QProfilePhoto.profilePhoto;
import static ccc.keewedomain.persistence.domain.user.QUser.user;

@Repository
@RequiredArgsConstructor
public class InsightQueryRepository {
    private final JPAQueryFactory queryFactory;

    public List<Insight> findAllByWriterId(Long writerId) {
        return queryFactory.select(insight)
                .from(insight)
                .where(insight.writer.id.eq(writerId)
                        .and(insight.deleted.isFalse()))
                .fetch();
    }

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

    public List<Insight> findAllValidByParticipation(ChallengeParticipation participation) {
        return queryFactory.select(insight)
                .from(insight)
                .leftJoin(insight.comments, comment)
                .fetchJoin()
                .where(insight.challengeParticipation.eq(participation)
                        .and(insight.deleted.isFalse())
                        .and(insight.valid.isTrue()))
                .distinct()
                .fetch();
    }

    public Long countValidByParticipation(ChallengeParticipation participation) {
        return queryFactory.select(insight.id.count())
                .from(insight)
                .where(insight.challengeParticipation.id.eq(participation.getId())
                        .and(insight.deleted.isFalse())
                        .and(insight.valid.isTrue()))
                .fetchFirst();
    }

    public Map<Long, Long> countValidPerParticipation(List<ChallengeParticipation> participations) {
        return queryFactory.select(insight.challengeParticipation.id, insight.id.count())
                .from(insight)
                .where(insight.challengeParticipation.in(participations)
                        .and(insight.valid.isTrue())
                )
                .groupBy(insight.challengeParticipation.id)
                .transform(GroupBy.groupBy(insight.challengeParticipation.id).as(insight.count()));
    }

    public List<Insight> findAllForHome(User user, CursorPageable<Long> cPage, Boolean follow) {
        BooleanExpression followFilter = Expressions.asBoolean(true).isTrue();
        if (follow != null && follow)
            followFilter = insight.writer.id.in(findFolloweesId(user));

        return queryFactory.select(insight)
                .from(insight)
                .where(followFilter
                        .and(insight.id.lt(cPage.getCursor()))
                        .and(insight.deleted.isFalse())
                )
                .innerJoin(insight.writer, QUser.user).fetchJoin()
                .leftJoin(QUser.user.profilePhoto, profilePhoto).fetchJoin()
                .leftJoin(QUser.user.repTitle, title).fetchJoin()
                .orderBy(insight.id.desc())
                .limit(cPage.getLimit())
                .fetch();
    }

    private JPQLQuery<Long> findFolloweesId(User user) {
        return JPAExpressions.select(follow.followee.id)
                .from(follow)
                .where(follow.follower.id.eq(user.getId()));
    }

    public Long countByParticipationBetween(ChallengeParticipation participation, LocalDateTime startDate, LocalDateTime endDate) {
        return queryFactory.select(insight.id.count())
                .from(insight)
                .where(insight.challengeParticipation.id.eq(participation.getId())
                        .and(insight.deleted.isFalse())
                        .and(insight.valid.isTrue())
                        .and(insight.createdAt.goe(startDate).and(insight.createdAt.lt(endDate))))
                .fetchFirst();
    }

    public Optional<Insight> findByIdWithParticipationAndChallenge(Long insightId) {
        return Optional.ofNullable(queryFactory.select(insight)
                .from(insight)
                .leftJoin(insight.challengeParticipation, challengeParticipation)
                .fetchJoin()
                .leftJoin(challengeParticipation.challenge, challenge)
                .fetchJoin()
                .where(insight.id.eq(insightId))
                .fetchOne());
    }

    // note. 참여중인 챌린지에 기록한 인사이트 수 조회
    public Long countValidByIdBeforeAndParticipation(ChallengeParticipation participation, Long insightId) {
        return queryFactory.select(insight.count())
                .from(insight)
                .where(insight.challengeParticipation.eq(participation)
                        .and(insight.valid.isTrue())
                        .and(insight.deleted.isFalse())
                        .and(insight.id.lt(insightId)))
                .fetchFirst();
    }

    // note. 서랍에 속한 인사이트 조회
    public List<Insight> findAllByUserIdAndDrawerId(Long userId, Long drawerId, CursorPageable<Long> cPage) {
        return queryFactory.select(insight)
                .from(insight)
                .where(insight.writer.id.eq(userId)
                        .and(insight.id.lt(cPage.getCursor()))
                        .and(drawerIdEq(drawerId))
                        .and(insight.deleted.isFalse())
                )
                .orderBy(insight.id.desc())
                .limit(cPage.getLimit())
                .fetch();
    }

    // 검색 관련 조회 쿼리
    public List<Insight> findAllByKeyword(String keyword, CursorPageable<Long> cPage) {
        return queryFactory.select(insight)
                .from(insight)
                .where(insight.contents.contains(keyword)
                        .and(insight.id.lt(cPage.getCursor()))
                        .and(insight.deleted.isFalse())
                )
                .orderBy(insight.id.desc())
                .limit(cPage.getLimit())
                .fetch();
    }

    public List<Insight> findAllByUserIdAndDrawerId(Long userId, Long drawerId) {
        return queryFactory.select(insight)
                .from(insight)
                .where(insight.writer.id.eq(userId)
                        .and(insight.drawer.id.eq(drawerId))
                )
                .fetch();
    }

    public Map<Long, Long> countPerChallenge(List<Challenge> challenges) {
        return queryFactory.from(insight)
                .innerJoin(insight.challengeParticipation, challengeParticipation)
                .innerJoin(challengeParticipation.challenge, challenge)
                .where(challenge.in(challenges)
                        .and(insight.deleted.isFalse())
                )
                .groupBy(challenge.id)
                .transform(GroupBy.groupBy(challenge.id).as(insight.count()));
    }

    public Long countByChallenge(Challenge target, Long writerId) {
        return queryFactory
                .select(insight.count())
                .from(insight)
                .innerJoin(insight.challengeParticipation, challengeParticipation)
                .innerJoin(challengeParticipation.challenge, challenge)
                .where(challenge.eq(target)
                        .and(writerIdEq(writerId))
                        .and(insight.deleted.isFalse())
                )
                .fetchFirst();
    }

    public boolean existByWriterAndCreatedAtBetweenAndValidTrue(User writer, LocalDateTime startDate, LocalDateTime endDate) {
        return queryFactory.selectOne()
                .from(insight)
                .where(insight.writer.eq(writer))
                .where(insight.createdAt.between(startDate, endDate))
                .where(insight.valid.isTrue())
                .where(insight.deleted.isFalse())
                .fetchFirst() != null;
    }

    public Map<Insight, LocalDateTime> findBookmarkedInsight(User user, CursorPageable<LocalDateTime> cPage) {
        List<Tuple> result = queryFactory.select(insight, bookmark.createdAt)
                .from(insight)
                .innerJoin(bookmark).on(insight.id.eq(bookmark.insight.id))
                .where(insight.deleted.isFalse())
                .where(bookmark.user.id.eq(user.getId()))
                .where(bookmark.createdAt.lt(cPage.getCursor()))
                .orderBy(bookmark.createdAt.desc())
                .limit(cPage.getLimit())
                .fetch();

        return result.stream()
                .collect(Collectors.toMap(
                        tuple -> tuple.get(0, Insight.class),
                        tuple -> tuple.get(1, LocalDateTime.class),
                        (oldValue, newValue) -> oldValue,
                        LinkedHashMap::new // note. LinkedHashMap 사용하여 순서 보장
                ));
    }

    public List<Insight> findByChallenge(Challenge challenge, CursorPageable<Long> cPage, Long writerId) {
        return queryFactory.select(insight)
                .from(insight)
                .innerJoin(insight.challengeParticipation, challengeParticipation)
                .innerJoin(insight.writer, user)
                .fetchJoin()
                .leftJoin(user.profilePhoto, profilePhoto)
                .fetchJoin()
                .where(insight.challengeParticipation.challenge.eq(challenge)
                        .and(insight.id.lt(cPage.getCursor()))
                        .and(writerIdEq(writerId))
                        .and(insight.deleted.isFalse())
                )
                .orderBy(insight.id.desc())
                .limit(cPage.getLimit())
                .fetch();
    }

    public Long countByChallenge(Challenge challenge) {
        return queryFactory
                .select(insight.count())
                .from(insight)
                .innerJoin(insight.challengeParticipation, challengeParticipation)
                .where(insight.challengeParticipation.challenge.eq(challenge)
                        .and(insight.deleted.isFalse()))
                .fetchOne();
    }

    private BooleanExpression drawerIdEq(Long drawerId) {
        return drawerId != null ? insight.drawer.id.eq(drawerId) : null;
    }

    private BooleanExpression writerIdEq(Long writerId) {
        return writerId != null ? insight.writer.id.eq(writerId) : null;
    }
}
