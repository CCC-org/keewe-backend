package ccc.keewedomain.persistence.repository.challenge;

import ccc.keewedomain.persistence.domain.challenge.enums.ChallengeParticipationStatus;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import static ccc.keewedomain.persistence.domain.challenge.QChallengeParticipation.challengeParticipation;
import static ccc.keewedomain.persistence.domain.user.QUser.user;

@RequiredArgsConstructor
public class ChallengeParticipationQueryRepositoryImpl implements ChallengeParticipationQueryRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public boolean existsByChallengerIdAndStatus(Long challengerId, ChallengeParticipationStatus status) {
        Integer fetchFirst = queryFactory
                .selectOne()
                .from(challengeParticipation)
                .where(user.id.eq(challengerId).and(challengeParticipation.status.eq(status)))
                .fetchFirst();

        return fetchFirst != null;
    }
}
