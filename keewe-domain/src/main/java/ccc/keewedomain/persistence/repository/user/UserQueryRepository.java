package ccc.keewedomain.persistence.repository.user;

import ccc.keewedomain.persistence.domain.user.User;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import static ccc.keewedomain.persistence.domain.common.QInterest.interest;
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
