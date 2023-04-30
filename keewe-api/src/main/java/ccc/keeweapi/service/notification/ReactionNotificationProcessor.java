package ccc.keeweapi.service.notification;

import ccc.keeweapi.dto.notification.NotificationResponse;
import ccc.keewedomain.persistence.domain.insight.Reaction;
import ccc.keewedomain.persistence.domain.notification.Notification;
import ccc.keewedomain.persistence.domain.notification.enums.NotificationCategory;
import ccc.keewedomain.persistence.domain.notification.enums.NotificationContents;
import ccc.keewedomain.persistence.domain.user.User;
import ccc.keewedomain.service.insight.ReactionDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class ReactionNotificationProcessor implements NotificationProcessor {
    private final ReactionDomainService reactionDomainService;

    @Override
    public NotificationCategory getCategory() {
        return NotificationCategory.REACTION;
    }

    @Override
    public NotificationResponse process(Notification notification) {
        String reactionId = notification.getReferenceId();

        Reaction reaction = reactionDomainService.getByIdOrElseThrow(Long.parseLong(reactionId));
        User user = reaction.getReactor();
        NotificationContents contents = notification.getContents();
        return NotificationResponse.of(
            notification.getId(),
            contents.getTitle(), // note. 내 인사이트에 누군가 반응 남김
            String.format(contents.getContents(), user.getNickname()), // note. {UserName}님이 반응을 남겼어요.
            contents.getCategory(),
            String.valueOf(reaction.getInsight().getId()), // note. 클릭 시 인사이트로 이동
            notification.isRead(),
            notification.getCreatedAt().toLocalDate().toString()
        );
    }
}
