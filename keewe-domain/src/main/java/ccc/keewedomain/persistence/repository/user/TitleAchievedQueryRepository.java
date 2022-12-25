package ccc.keewedomain.persistence.repository.user;

import ccc.keewedomain.persistence.domain.title.TitleAchievement;
import ccc.keewedomain.persistence.domain.user.User;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static ccc.keewedomain.persistence.domain.title.QTitle.title;
import static ccc.keewedomain.persistence.domain.title.QTitleAchievement.titleAchievement;

@Repository
@RequiredArgsConstructor
public class TitleAchievedQueryRepository {

    private final JPAQueryFactory queryFactory;

    public List<TitleAchievement> findByUserIdOrderByCreatedAtDesc(User target, int limit) {
        return queryFactory
                .select(titleAchievement)
                .from(titleAchievement)
                .innerJoin(titleAchievement.title, title)
                .fetchJoin()
                .where(titleAchievement.user.eq(target))
                .orderBy(titleAchievement.createdAt.desc())
                .limit(limit)
                .fetch();
    }
}
