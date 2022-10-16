package ccc.keewedomain.persistence.repository.challenge;

import ccc.keewedomain.persistence.domain.challenge.ChallengeParticipation;
import ccc.keewedomain.persistence.domain.challenge.enums.ChallengeParticipationStatus;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

import static ccc.keewedomain.persistence.domain.challenge.QChallenge.challenge;
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

    @Override
    public Optional<ChallengeParticipation> findByChallengerIdAndStatusWithChallenge (Long challengerId, ChallengeParticipationStatus status) {
        return Optional.ofNullable(queryFactory
                .select(challengeParticipation)
                .from(challengeParticipation)
                .innerJoin(challengeParticipation.challenge, challenge)
                .fetchJoin()
                .where(challengeParticipation.challenger.id.eq(challengerId))
                .fetchOne());
    }
}
