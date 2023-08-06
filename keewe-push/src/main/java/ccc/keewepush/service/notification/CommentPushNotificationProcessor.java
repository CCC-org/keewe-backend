package ccc.keewepush.service.notification;

import ccc.keewedomain.persistence.domain.insight.Comment;
import ccc.keewedomain.persistence.domain.notification.Notification;
import ccc.keewedomain.persistence.domain.notification.enums.NotificationCategory;
import ccc.keewedomain.service.insight.query.CommentQueryDomainService;
import ccc.keewepush.dto.PushMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class CommentPushNotificationProcessor implements PushNotificationProcessor {
    private final CommentQueryDomainService commentQueryDomainService;

    @Override
    public NotificationCategory getCategory() {
        return NotificationCategory.COMMENT;
    }

    @Override
    @Transactional(readOnly = true)
    public PushMessage process(Notification notification) {
        Long userId = notification.getUser().getId();
        String commentId = notification.getReferenceId();
        Comment comment = commentQueryDomainService.getByIdOrElseThrow(Long.parseLong(commentId));
        String contents = String.format(notification.getContents().getPushContents(), comment.getWriter().getNickname());
        return PushMessage.of(userId, contents);
    }
}
