package ccc.keewepush.service.notification;

import ccc.keewedomain.persistence.domain.notification.Notification;
import ccc.keewedomain.persistence.domain.notification.enums.NotificationCategory;
import ccc.keewedomain.persistence.domain.user.User;
import ccc.keewedomain.service.user.UserDomainService;
import ccc.keewepush.dto.PushMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class FollowPushNotificationProcessor implements PushNotificationProcessor {
    private final UserDomainService userDomainService;

    @Override
    public NotificationCategory getCategory() {
        return NotificationCategory.FOLLOW;
    }

    @Override
    @Transactional(readOnly = true)
    public PushMessage process(Notification notification) {
        User user = notification.getUser();
        String followerId = notification.getReferenceId();
        User follower = userDomainService.getUserByIdOrElseThrow(Long.parseLong(followerId));
        String contents = String.format(notification.getContents().getPushContents(), follower.getNickname());
        return PushMessage.of(user.getId(), contents);
    }
}
