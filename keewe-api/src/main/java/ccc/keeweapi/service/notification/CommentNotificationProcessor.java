package ccc.keeweapi.service.notification;

import ccc.keeweapi.dto.notification.NotificationResponse;
import ccc.keewecore.utils.KeeweStringUtils;
import ccc.keewedomain.persistence.domain.insight.Comment;
import ccc.keewedomain.persistence.domain.notification.Notification;
import ccc.keewedomain.persistence.domain.notification.enums.NotificationCategory;
import ccc.keewedomain.persistence.domain.notification.enums.NotificationContents;
import ccc.keewedomain.service.insight.query.CommentQueryDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class CommentNotificationProcessor implements NotificationProcessor {
    private final CommentQueryDomainService commentQueryDomainService;

    @Override
    public NotificationCategory getCategory() {
        return NotificationCategory.COMMENT;
    }

    @Override
    public NotificationResponse process(Notification notification) {
        String commentId = notification.getReferenceId();
        Comment comment = commentQueryDomainService.getByIdOrElseThrow(Long.parseLong(commentId));
        String commentContents = comment.getContent();
        String notificationTitle = KeeweStringUtils.getOrWithEllipsis(commentContents, 20);
        NotificationContents contents = notification.getContents();
        return NotificationResponse.of(
            notification.getId(),
            notificationTitle,
            String.format(contents.getContents(), comment.getWriter().getNickname()), // note. {UserName}님이 댓글을 남겼어요.
            contents.getCategory(),
            String.valueOf(comment.getInsight().getId()),  // note. 클릭 시 인사이트로 이동
            notification.isRead(),
            notification.getCreatedAt().toLocalDate().toString()
        );
    }
}
