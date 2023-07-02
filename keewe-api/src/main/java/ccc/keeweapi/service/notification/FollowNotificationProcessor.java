package ccc.keeweapi.service.notification;

import ccc.keeweapi.dto.notification.NotificationResponse;
import ccc.keewedomain.persistence.domain.notification.Notification;
import ccc.keewedomain.persistence.domain.notification.enums.NotificationCategory;
import ccc.keewedomain.persistence.domain.notification.enums.NotificationContents;
import ccc.keewedomain.persistence.domain.user.User;
import ccc.keewedomain.service.user.UserDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class FollowNotificationProcessor implements NotificationProcessor {
    private final UserDomainService userDomainService;

    @Override
    public NotificationCategory getCategory() {
        return NotificationCategory.FOLLOW;
    }

    @Override
    public NotificationResponse process(Notification notification) {
        String followerId = notification.getReferenceId();

        User follower = userDomainService.getUserByIdOrElseThrow(Long.parseLong(followerId));
        NotificationContents contents = notification.getContents();
        return NotificationResponse.of(
            notification.getId(),
            follower.getNickname(), // note. {UserName}
            String.format(contents.getContents(), follower.getNickname()), // note. {UserName}님이 반응을 남겼어요.
            contents.getCategory(),
            followerId, // note. 클릭 시 프로필로 이동
            notification.isRead(),
            notification.getCreatedAt().toLocalDate().toString()
        );
    }
}
