package ccc.keewedomain.service.notification.command;

import ccc.keewecore.consts.KeeweRtnConsts;
import ccc.keewecore.exception.KeeweException;
import ccc.keewedomain.persistence.domain.notification.Notification;
import ccc.keewedomain.persistence.repository.notification.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class NotificationCommandDomainService {
    private final NotificationRepository notificationRepository;

    @Transactional
    public Notification markAsRead(Long notificationId) {
        return notificationRepository.findById(notificationId)
                .map(notification -> notificationRepository.save(notification.markAsRead()))
                .orElseThrow(() -> new KeeweException(KeeweRtnConsts.ERR483));
    }
}
