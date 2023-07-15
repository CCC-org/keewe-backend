package ccc.keeweapi.service.challenge.command;

import ccc.keeweapi.utils.SecurityUtil;
import ccc.keewedomain.persistence.domain.notification.Notification;
import ccc.keewedomain.persistence.domain.notification.enums.NotificationContents;
import ccc.keewedomain.persistence.domain.user.User;
import ccc.keewedomain.service.notification.command.NotificationCommandDomainService;
import ccc.keewedomain.service.user.UserDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChallengeInviteApiService {
    private final NotificationCommandDomainService notificationCommandDomainService;
    private final UserDomainService userDomainService;

    public void sendInvite(Long targetUserId, Long challengeId) {
        User user = userDomainService.getUserByIdOrElseThrow(targetUserId);
        String extra = SecurityUtil.getUserId() + "," + challengeId;
        Notification notification = Notification.of(user, NotificationContents.챌린지_초대, extra);
        notificationCommandDomainService.save(notification);
    }
}
