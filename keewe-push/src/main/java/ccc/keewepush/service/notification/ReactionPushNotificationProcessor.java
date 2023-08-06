package ccc.keewepush.service.notification;

import ccc.keewecore.utils.KeeweStringUtils;
import ccc.keewedomain.persistence.domain.insight.Reaction;
import ccc.keewedomain.persistence.domain.notification.Notification;
import ccc.keewedomain.persistence.domain.notification.enums.NotificationCategory;
import ccc.keewedomain.persistence.domain.user.User;
import ccc.keewedomain.service.insight.ReactionDomainService;
import ccc.keewepush.dto.PushMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class ReactionPushNotificationProcessor implements PushNotificationProcessor {
    private final ReactionDomainService reactionDomainService;

    @Override
    public NotificationCategory getCategory() {
        return NotificationCategory.REACTION;
    }

    @Override
    @Transactional(readOnly = true)
    public PushMessage process(Notification notification) {
        User user = notification.getUser();
        String reactionId = notification.getReferenceId();
        Reaction reaction = reactionDomainService.getByIdOrElseThrow(Long.parseLong(reactionId));
        User reactor = reaction.getReactor();
        String insightContents = KeeweStringUtils.getOrWithEllipsis(reaction.getInsight().getContents(), 20);
        return PushMessage.of(
                user.getId(),
                String.format(notification.getContents().getPushContents(), reactor.getNickname(), insightContents)
        );
    }
}
