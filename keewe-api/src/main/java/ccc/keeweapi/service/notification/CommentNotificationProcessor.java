package ccc.keeweapi.service.notification;

import ccc.keeweapi.dto.notification.NotificationResponse;
import ccc.keewedomain.persistence.domain.insight.Comment;
import ccc.keewedomain.persistence.domain.notification.Notification;
import ccc.keewedomain.persistence.domain.notification.enums.NotificationCategory;
import ccc.keewedomain.persistence.domain.notification.enums.NotificationContents;
import ccc.keewedomain.service.insight.CommentDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class CommentNotificationProcessor implements NotificationProcessor {
    private final CommentDomainService commentDomainService;

    @Override
    public NotificationCategory getCategory() {
        return NotificationCategory.COMMENT;
    }

    @Override
    public NotificationResponse process(Notification notification) {
        String commentId = notification.getReferenceId();
        Comment comment = commentDomainService.getByIdOrElseThrow(Long.parseLong(commentId));
        NotificationContents contents = notification.getContents();
        return NotificationResponse.of(
            notification.getId(),
            notification.getContents().getTitle(),
            String.format(contents.getContents(), comment.getWriter().getNickname()), // note. {UserName}님이 댓글을 남겼어요.
            contents.getCategory(),
            notification.getReferenceId(),
            notification.isRead()
        );
    }
}
