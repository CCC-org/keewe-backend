package ccc.keewedomain.persistence.repository.user;

import ccc.keewedomain.persistence.domain.user.User;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

import static ccc.keewedomain.persistence.domain.user.QFollow.follow;

@Repository
@RequiredArgsConstructor
public class FollowQueryRepository {

    private final JPAQueryFactory queryFactory;

    public Set<Long> existsByIds(User user, List<User> targets) {
        return Set.copyOf(queryFactory
                .select(follow.followee.id)
                .from(follow)
                .where(follow.follower.eq(user).and(follow.followee.in((targets))))
                .fetch()
        );
    }
}
