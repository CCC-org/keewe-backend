package ccc.keeweapi.service.notification;

import ccc.keeweapi.dto.notification.NotificationResponse;
import ccc.keewecore.consts.KeeweRtnConsts;
import ccc.keewecore.exception.KeeweException;
import ccc.keewedomain.persistence.domain.challenge.Challenge;
import ccc.keewedomain.persistence.domain.notification.Notification;
import ccc.keewedomain.persistence.domain.notification.enums.NotificationCategory;
import ccc.keewedomain.persistence.domain.notification.enums.NotificationContents;
import ccc.keewedomain.service.challenge.query.ChallengeQueryDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ChallengeNotificationProcessor implements NotificationProcessor {
    private final ChallengeQueryDomainService challengeQueryDomainService;

    @Override
    public NotificationCategory getCategory() {
        return NotificationCategory.CHALLENGE;
    }

    @Override
    public NotificationResponse process(Notification notification) {
        NotificationContents contents = notification.getContents();
        Long challengeId = Long.parseLong(notification.getReferenceId());
        Challenge challenge = challengeQueryDomainService.getByIdOrElseThrow(challengeId);
        return NotificationResponse.of(
                notification.getId(),
                challenge.getName(),
                this.getNotificationContents(challenge, contents),
                contents.getCategory(),
                challengeId.toString(),
                notification.isRead(),
                notification.getCreatedAt().toString()
        );
    }

    private String getNotificationContents(Challenge challenge, NotificationContents notificationContents) {
        switch (notificationContents) {
            case 챌린지_성공:
            case 챌린지_채찍:
                return notificationContents.getContents();
            case 챌린지_실패:
                return String.format(notificationContents.getContents(), challenge.getName());
            default:
                throw new KeeweException(KeeweRtnConsts.ERR999);
        }
    }
}
