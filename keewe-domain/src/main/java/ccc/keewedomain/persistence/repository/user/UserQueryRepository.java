package ccc.keewedomain.persistence.repository.user;

import ccc.keewedomain.persistence.domain.common.Interest;
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
import java.util.Optional;

import static ccc.keewedomain.persistence.domain.common.QInterest.interest;
import static ccc.keewedomain.persistence.domain.title.QTitle.title;
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
}
