package ccc.keewedomain.persistence.repository.challenge;

import static ccc.keewedomain.persistence.domain.challenge.QChallenge.challenge;

import ccc.keewedomain.persistence.domain.challenge.Challenge;
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
}
