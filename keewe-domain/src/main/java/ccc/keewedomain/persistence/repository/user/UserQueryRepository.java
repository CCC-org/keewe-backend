package ccc.keewedomain.persistence.repository.user;

import static ccc.keewedomain.persistence.domain.common.QInterest.interest;
import static ccc.keewedomain.persistence.domain.user.QUser.user;

import ccc.keewedomain.persistence.domain.user.User;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

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
                .where(user.id.eq(userId))
                .fetchFirst());
    }
}
