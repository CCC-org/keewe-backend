package ccc.keewedomain.persistence.repository.challenge;

import static ccc.keewedomain.persistence.domain.challenge.QChallenge.challenge;

import ccc.keewedomain.persistence.domain.challenge.Challenge;
import ccc.keewedomain.persistence.repository.utils.CursorPageable;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ChallengeQueryRepository {
    private final JPAQueryFactory queryFactory;

    public List<Challenge> getSpecifiedNumberOfChallenge(int size) {
        return queryFactory.select(challenge)
                .from(challenge)
                .where(challenge.deleted.isFalse())
                .orderBy(challenge.id.desc())
                .limit(size)
                .fetch();
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
}
