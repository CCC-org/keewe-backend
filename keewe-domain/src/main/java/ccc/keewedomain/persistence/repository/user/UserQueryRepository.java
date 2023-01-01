package ccc.keewedomain.persistence.repository.user;

import ccc.keewedomain.persistence.domain.user.Follow;
import ccc.keewedomain.persistence.domain.user.User;
import ccc.keewedomain.persistence.repository.utils.CursorPageable;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static ccc.keewedomain.persistence.domain.common.QInterest.interest;
import static ccc.keewedomain.persistence.domain.user.QFollow.follow;
import static ccc.keewedomain.persistence.domain.user.QUser.user;

@Repository
@RequiredArgsConstructor
public class UserQueryRepository {

    private final JPAQueryFactory queryFactory;

    public Optional<User> findByIdWithInterests(Long userId) {
        return Optional.ofNullable(queryFactory
                .select(user)
                .from(user)
                .leftJoin(user.interests, interest)
                .fetchJoin()
                .where(user.id.eq(userId)
                        .and(user.deleted.isFalse()))
                .fetchFirst());
    }

    public List<Follow> findFollowersByUserCreatedAtDesc(User target, CursorPageable<LocalDateTime> cPage) {
        return queryFactory
                .select(follow)
                .from(follow)
                .innerJoin(follow.follower, user)
                .fetchJoin()
                .where(follow.followee.eq(target)
                        .and(followCreatedAtLt(cPage.getCursor())))
                .orderBy(follow.createdAt.desc(), follow.followee.id.asc())
                .limit(cPage.getLimit())
                .fetch();
    }

    public List<Follow> findFolloweesByUserCreatedAtDesc(User target, CursorPageable<LocalDateTime> cPage) {
        return queryFactory
                .select(follow)
                .from(follow)
                .innerJoin(follow.followee, user)
                .fetchJoin()
                .where(follow.follower.eq(target)
                        .and(followCreatedAtLt(cPage.getCursor())))
                .orderBy(follow.createdAt.desc(), follow.followee.id.asc())
                .limit(cPage.getLimit())
                .fetch();
    }

    private BooleanExpression followCreatedAtLt(LocalDateTime cursor) {
        return cursor != null ? follow.createdAt.lt(cursor) : null;
    }
}
