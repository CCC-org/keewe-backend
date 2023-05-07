package ccc.keewedomain.persistence.repository.notification;

import static ccc.keewedomain.persistence.domain.notification.QNotification.notification;

import ccc.keewedomain.persistence.domain.notification.Notification;
import ccc.keewedomain.persistence.domain.user.User;
import ccc.keewedomain.persistence.repository.utils.CursorPageable;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class NotificationQueryRepository {
    private final JPAQueryFactory queryFactory;

    public List<Notification> paginate(CursorPageable<Long> cPage, User user) {
        return queryFactory.selectFrom(notification)
                .innerJoin(notification.user)
                .fetchJoin()
                .where(notification.user.eq(user)
                        .and(notification.id.lt(cPage.getCursor()))
                )
                .limit(cPage.getLimit())
                .orderBy(notification.id.desc())
                .fetch();
    }

    public Boolean isUnreadNotificationExist(User user) {
        return queryFactory.selectFrom(notification)
                .where(notification.user.eq(user)
                        .and(notification.isRead.eq(false))
                )
                .fetchFirst() != null;
    }
}
