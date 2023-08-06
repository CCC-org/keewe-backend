package ccc.keewedomain.service.notification.query;

import ccc.keewecore.consts.KeeweRtnConsts;
import ccc.keewecore.exception.KeeweException;
import ccc.keewedomain.persistence.domain.notification.Notification;
import ccc.keewedomain.persistence.domain.user.User;
import ccc.keewedomain.persistence.repository.notification.NotificationQueryRepository;
import ccc.keewedomain.persistence.repository.notification.NotificationRepository;
import ccc.keewedomain.persistence.repository.utils.CursorPageable;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class NotificationQueryDomainService {
    private final NotificationQueryRepository notificationQueryRepository;
    private final NotificationRepository notificationRepository;

    public List<Notification> paginateNotifications(CursorPageable<Long> cPage, User user) {
        return notificationQueryRepository.paginate(cPage, user);
    }

    public Boolean isUnreadNotificationExist(User user) {
        return notificationQueryRepository.isUnreadNotificationExist(user);
    }

    public Notification findByIdOrElseThrow(Long notificationId) {
        return notificationRepository.findById(notificationId)
                .orElseThrow(() -> new KeeweException(KeeweRtnConsts.ERR483));
    }
}
