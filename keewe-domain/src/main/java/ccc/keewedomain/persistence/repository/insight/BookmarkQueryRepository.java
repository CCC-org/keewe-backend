package ccc.keewedomain.persistence.repository.insight;

import static ccc.keewedomain.persistence.domain.insight.QBookmark.bookmark;
import ccc.keewedomain.persistence.domain.insight.Insight;
import ccc.keewedomain.persistence.domain.user.User;
import com.querydsl.core.group.GroupBy;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class BookmarkQueryRepository {

    private final JPAQueryFactory queryFactory;

    public Map<Long, Boolean> getBookmarkPresenceMap(User user, List<Insight> insightList) {
        return queryFactory.from(bookmark)
                .where(bookmark.user.eq(user))
                .where(bookmark.insight.in(insightList))
                .groupBy(bookmark.insight.id)
                .transform(GroupBy.groupBy(bookmark.insight.id).as(bookmark.isNotNull()));
    }

    public Long countBookmark(List<Insight> insights) {
        return queryFactory.selectFrom(bookmark)
                .where(bookmark.insight.in(insights))
                .fetchCount();
    }

    public Long countBookmarkByInsightId(Long insightId) {
        return queryFactory.select(bookmark.count())
                .from(bookmark)
                .where(bookmark.insight.id.eq(insightId))
                .fetchFirst();
    }

    public Long countByUserId(Long userId) {
        return queryFactory.select(bookmark.count())
                .from(bookmark)
                .where(bookmark.user.id.eq(userId))
                .fetchFirst();
    }
}
