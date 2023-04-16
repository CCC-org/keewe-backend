package ccc.keewedomain.persistence.repository.insight;

import static ccc.keewedomain.persistence.domain.insight.QReaction.reaction;

import ccc.keewedomain.persistence.domain.insight.Insight;
import ccc.keewedomain.persistence.domain.user.User;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
@Slf4j
public class ReactionQueryRepository {

    private final JPAQueryFactory queryFactory;

    public Long countsAllByInsightAndReactor(Insight insight, User reactor) {
        return queryFactory.select(reaction.count())
                .from(reaction)
                .where(reaction.insight.eq(insight))
                .where(reaction.reactor.eq(reactor))
                .fetchFirst();
    }
}
