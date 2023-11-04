package ccc.keewedomain.persistence.repository.challenge;

import static ccc.keewedomain.persistence.domain.challenge.QChallenge.challenge;
import static ccc.keewedomain.persistence.domain.user.QUser.user;

import ccc.keewedomain.persistence.domain.challenge.Challenge;
import ccc.keewedomain.persistence.domain.user.User;
import ccc.keewedomain.persistence.repository.utils.CursorPageable;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ChallengeQueryRepository {
    private final JPAQueryFactory queryFactory;

    public Long countCreatedChallengeByUser(User user) {
        return queryFactory.select(challenge.count())
                .from(challenge)
                .where(challenge.writer.eq(user))
                .fetchOne();
    }

    public List<Challenge> paginate(CursorPageable<Long> cPage) {
        return queryFactory.select(challenge)
                .from(challenge)
                .where(challenge.deleted.isFalse()
                        .and(challenge.id.lt(cPage.getCursor()))
                )
                .orderBy(challenge.id.desc())
                .limit(cPage.getLimit())
                .fetch();
    }

    // 검색 관련 조회 쿼리
    public List<Challenge> findAllByKeyword(String keyword, CursorPageable<Long> cPage) {
        return queryFactory.select(challenge)
                .from(challenge)
                .where(challenge.name.contains(keyword)
                        .and(challenge.id.lt(cPage.getCursor()))
                        .and(challenge.deleted.isFalse())
                )
                .orderBy(challenge.id.desc())
                .limit(cPage.getLimit())
                .fetch();
    }
}
