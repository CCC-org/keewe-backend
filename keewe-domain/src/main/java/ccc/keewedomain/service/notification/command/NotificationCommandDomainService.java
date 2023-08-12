package ccc.keewedomain.service.notification.command;

import ccc.keewecore.consts.KeeweRtnConsts;
import ccc.keewecore.exception.KeeweException;
import ccc.keewedomain.event.notification.NotificationCreateEvent;
import ccc.keewedomain.persistence.domain.notification.Notification;
import ccc.keewedomain.persistence.repository.notification.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class NotificationCommandDomainService {
    private final NotificationRepository notificationRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Transactional
    public Notification save(Notification notification) {
        boolean isFirstCreated = notification.getId() == null;
        Notification savedNotification = notificationRepository.save(notification);
        NotificationCreateEvent event = NotificationCreateEvent.of(savedNotification);
        // note. 최초 생성되는 알림에 대해서만 이벤트 발행
        if (isFirstCreated) {
            eventPublisher.publishEvent(event);
        }
        return savedNotification;
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
