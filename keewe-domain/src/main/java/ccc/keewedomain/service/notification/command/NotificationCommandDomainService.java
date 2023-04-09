package ccc.keewedomain.service.notification.command;

import ccc.keewecore.consts.KeeweRtnConsts;
import ccc.keewecore.exception.KeeweException;
import ccc.keewedomain.persistence.domain.notification.Notification;
import ccc.keewedomain.persistence.repository.notification.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class NotificationCommandDomainService {
    private final NotificationRepository notificationRepository;

    public Notification save(Notification notification) {
        return notificationRepository.save(notification);
    }

    public Notification getByIdWithUserAssert(Long notificationId, Long userId) {
        return notificationRepository.findById(notificationId)
                .map(notification -> {
                    if(!notification.getUser().getId().equals(userId)) {
                        throw new KeeweException(KeeweRtnConsts.ERR404);
                    }
                    return notification;
                })
                .orElseThrow(() -> new KeeweException(KeeweRtnConsts.ERR483));
    }
}
