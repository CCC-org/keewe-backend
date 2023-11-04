package ccc.keewedomain.persistence.repository.user;

import static ccc.keewedomain.persistence.domain.common.QInterest.interest;
import static ccc.keewedomain.persistence.domain.insight.QInsight.insight;
import static ccc.keewedomain.persistence.domain.user.QUser.user;

import ccc.keewedomain.persistence.domain.insight.Insight;
import ccc.keewedomain.persistence.domain.user.User;
import ccc.keewedomain.persistence.repository.utils.CursorPageable;
import com.querydsl.jpa.impl.JPAQueryFactory;

import java.util.List;
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

    // 검색 관련 조회 쿼리
    public List<User> findAllByKeyword(String keyword, CursorPageable<Long> cPage) {
        return queryFactory.select(user)
                .from(user)
                .where(user.nickname.contains(keyword)
                        .and(user.id.lt(cPage.getCursor()))
                        .and(user.deleted.isFalse())
                )
                .orderBy(user.id.desc())
                .limit(cPage.getLimit())
                .fetch();
    }
}
