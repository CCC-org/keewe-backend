package ccc.keeweapi.service.notification;

import ccc.keeweapi.dto.notification.NotificationResponse;
import ccc.keewedomain.persistence.domain.challenge.Challenge;
import ccc.keewedomain.persistence.domain.notification.Notification;
import ccc.keewedomain.persistence.domain.notification.enums.NotificationCategory;
import ccc.keewedomain.persistence.domain.notification.enums.NotificationContents;
import ccc.keewedomain.persistence.domain.user.User;
import ccc.keewedomain.service.challenge.query.ChallengeQueryDomainService;
import ccc.keewedomain.service.user.UserDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ChallengeInviteNotificationProcessor implements NotificationProcessor {
    private final ChallengeQueryDomainService challengeQueryDomainService;
    private final UserDomainService userDomainService;

    @Override
    public NotificationCategory getCategory() {
        return NotificationCategory.CHALLENGE_INVITE;
    }

    @Override
    public NotificationResponse process(Notification notification) {
        NotificationContents contents = notification.getContents();
        String[] extra = notification.getReferenceId().split(",");
        Long fromUserId = Long.parseLong(extra[0]);
        Long challengeId = Long.parseLong(extra[1]);
        User fromUser = userDomainService.getUserByIdOrElseThrow(fromUserId);
        Challenge challenge = challengeQueryDomainService.getByIdOrElseThrow(challengeId);
        return NotificationResponse.of(
                notification.getId(),
                fromUser.getNickname(),
                String.format(contents.getContents().trim(), challenge.getName()),
                contents.getCategory(),
                challengeId.toString(),
                notification.isRead(),
                notification.getCreatedAt().toString()
        );
    }
}
