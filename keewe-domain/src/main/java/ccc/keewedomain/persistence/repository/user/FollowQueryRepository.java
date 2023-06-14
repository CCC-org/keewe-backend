package ccc.keewedomain.persistence.repository.user;

import ccc.keewedomain.persistence.domain.user.QUser;
import ccc.keewedomain.persistence.repository.user.cursor.InviteeSearchCursor;
import ccc.keewedomain.persistence.domain.user.Follow;
import ccc.keewedomain.persistence.domain.user.User;
import ccc.keewedomain.persistence.repository.utils.CursorPageable;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

import static ccc.keewedomain.persistence.domain.title.QTitle.title;
import static ccc.keewedomain.persistence.domain.user.QFollow.follow;
import static ccc.keewedomain.persistence.domain.user.QUser.user;

@Repository
@RequiredArgsConstructor
public class FollowQueryRepository {

    private final JPAQueryFactory queryFactory;

    public List<Long> findFollowingTargetIds(User user, List<User> targets) {
        return queryFactory.select(follow.followee.id)
                .from(follow)
                .where(follow.follower.eq(user).and(follow.followee.in((targets))))
                .fetch();
    }

    public List<Follow> findFollowersByUserCreatedAtDesc(User target, CursorPageable<LocalDateTime> cPage) {
        return queryFactory.select(follow)
                .from(follow)
                .innerJoin(follow.follower, user)
                .fetchJoin()
                .leftJoin(user.repTitle, title)
                .fetchJoin()
                .where(follow.follower.id.in(findFollowerIdsOrderByCreatedAtDesc(target, cPage)),
                        follow.followee.eq(target))
                .fetch();
    }

    public List<Follow> findFolloweesByUserCreatedAtDesc(User target, CursorPageable<LocalDateTime> cPage) {
        return queryFactory.select(follow)
                .from(follow)
                .innerJoin(follow.followee, user)
                .fetchJoin()
                .leftJoin(user.repTitle, title)
                .fetchJoin()
                .where(follow.followee.id.in(findFolloweeIdsOrderByCreatedAtDesc(target, cPage)),
                        follow.follower.eq(target))
                .fetch();
    }

    public List<Follow> findAllByUserIdOrderByCreatedAtDesc(Long userId, CursorPageable<LocalDateTime> cPage) {
        return queryFactory.selectFrom(follow)
                .where(follow.follower.id.eq(userId)
                        .or(follow.followee.id.eq(userId)))
                .where(follow.createdAt.lt(cPage.getCursor()))
                .orderBy(follow.createdAt.desc())
                .limit(cPage.getLimit())
                .fetch();
    }

    public List<Follow> findByUserIdAndStartsWithNickname(Long userId, String word, CursorPageable<InviteeSearchCursor> cPage) {
        QUser user1 = new QUser("user1");
        QUser user2 = new QUser("user2");
        return queryFactory.selectFrom(follow)
                .innerJoin(follow.follower, user1)
                .fetchJoin()
                .innerJoin(follow.followee, user2)
                .fetchJoin()
                .where(follow.follower.id.eq(userId)
                        .or(follow.followee.id.eq(userId)))
                .where(user1.nickname.startsWith(word).or(user2.nickname.startsWith(word)))
                .where(nicknameGt(cPage.getCursor().getNickname()))
                .where(userIdGt(cPage.getCursor().getUserId()))
                .limit(cPage.getLimit())
                .fetch();
    }

    private BooleanExpression userIdGt(Long userId) {
        return userId != null ? user.id.gt(userId) : null;
    }

    // note. cursor가 null인 경우 조건을 추가하지 않음
    private BooleanExpression nicknameGt(String nickname) {
        return nickname != null ? user.nickname.gt(nickname) : null;
    }

    private JPQLQuery<Long> findFolloweeIdsOrderByCreatedAtDesc(User target, CursorPageable<LocalDateTime> cPage) {
        return JPAExpressions.select(follow.followee.id)
                .from(follow)
                .where(follow.follower.eq(target), follow.createdAt.lt(cPage.getCursor()))
                .orderBy(follow.createdAt.desc(), follow.followee.id.asc())
                .limit(cPage.getLimit());
    }

    private JPQLQuery<Long> findFollowerIdsOrderByCreatedAtDesc(User target, CursorPageable<LocalDateTime> cPage) {
        return JPAExpressions.select(follow.follower.id)
                .from(follow)
                .where(follow.followee.eq(target), follow.createdAt.lt(cPage.getCursor()))
                .orderBy(follow.createdAt.desc(), follow.follower.id.asc())
                .limit(cPage.getLimit());
    }
}
