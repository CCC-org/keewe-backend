package ccc.keewedomain.persistence.repository.user;

import ccc.keewedomain.persistence.domain.user.Follow;
import ccc.keewedomain.persistence.domain.user.User;
import ccc.keewedomain.persistence.repository.utils.CursorPageable;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import static ccc.keewedomain.persistence.domain.title.QTitle.title;
import static ccc.keewedomain.persistence.domain.user.QFollow.follow;
import static ccc.keewedomain.persistence.domain.user.QProfilePhoto.profilePhoto;
import static ccc.keewedomain.persistence.domain.user.QUser.user;

@Repository
@RequiredArgsConstructor
public class FollowQueryRepository {

    private final JPAQueryFactory queryFactory;

    public List<Long> findFollowingTargetIds(User user, List<User> targets) {
        return queryFactory
                .select(follow.followee.id)
                .from(follow)
                .where(follow.follower.eq(user).and(follow.followee.in((targets))))
                .fetch();
    }

    public List<Follow> findFollowersByUserCreatedAtDesc(User target, CursorPageable<LocalDateTime> cPage) {
        return queryFactory
                .select(follow)
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
        return queryFactory
                .select(follow)
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
                .innerJoin(follow.followee, user)
                .innerJoin(follow.follower, user)
                .innerJoin(follow.follower.profilePhoto, profilePhoto)
                .innerJoin(follow.followee.profilePhoto, profilePhoto)
                .where(follow.follower.id.eq(userId)
                        .or(follow.followee.id.eq(userId)))
                .where(follow.createdAt.lt(cPage.getCursor()))
                .orderBy(follow.createdAt.desc())
                .limit(cPage.getLimit())
                .fetch();
    }

    private JPQLQuery<Long> findFolloweeIdsOrderByCreatedAtDesc(User target, CursorPageable<LocalDateTime> cPage) {
        return JPAExpressions
                .select(follow.followee.id)
                .from(follow)
                .where(follow.follower.eq(target), follow.createdAt.lt(cPage.getCursor()))
                .orderBy(follow.createdAt.desc(), follow.followee.id.asc())
                .limit(cPage.getLimit());
    }

    private JPQLQuery<Long> findFollowerIdsOrderByCreatedAtDesc(User target, CursorPageable<LocalDateTime> cPage) {
        return JPAExpressions
                .select(follow.follower.id)
                .from(follow)
                .where(follow.followee.eq(target), follow.createdAt.lt(cPage.getCursor()))
                .orderBy(follow.createdAt.desc(), follow.follower.id.asc())
                .limit(cPage.getLimit());
    }
}
