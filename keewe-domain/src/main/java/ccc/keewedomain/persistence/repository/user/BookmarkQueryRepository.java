package ccc.keewedomain.persistence.repository.user;

import ccc.keewedomain.persistence.domain.insight.Insight;
import ccc.keewedomain.persistence.domain.user.User;
import com.querydsl.core.group.GroupBy;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

import static ccc.keewedomain.persistence.domain.insight.QBookmark.bookmark;

@Repository
@RequiredArgsConstructor
public class BookmarkQueryRepository {

    private final JPAQueryFactory queryFactory;

    public Map<Long, Boolean> getBookmarkPresenceMap(User user, List<Insight> insightList) {
        return queryFactory
                .from(bookmark)
                .where(bookmark.user.eq(user))
                .where(bookmark.insight.in(insightList))
                .groupBy(bookmark.insight.id)
                .transform(GroupBy.groupBy(bookmark.insight.id).as(bookmark.isNotNull()));
    }

}
