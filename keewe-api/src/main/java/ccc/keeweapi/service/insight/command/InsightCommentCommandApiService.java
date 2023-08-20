package ccc.keeweapi.service.insight.command;

import ccc.keeweapi.component.CommentAssembler;
import ccc.keeweapi.component.InsightAssembler;
import ccc.keeweapi.dto.insight.request.CommentCreateRequest;
import ccc.keeweapi.dto.insight.response.CommentCreateResponse;
import ccc.keeweapi.dto.insight.response.CommentDeleteResponse;
import ccc.keewedomain.persistence.domain.insight.Comment;
import ccc.keewedomain.persistence.domain.notification.Notification;
import ccc.keewedomain.persistence.domain.notification.enums.NotificationContents;
import ccc.keewedomain.persistence.domain.user.User;
import ccc.keewedomain.service.insight.command.CommentCommandDomainService;
import ccc.keewedomain.service.notification.command.NotificationCommandDomainService;
import ccc.keeweinfra.service.messagequeue.MQPublishService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

@Service
@RequiredArgsConstructor
@Slf4j
public class InsightCommentCommandApiService {

    private final CommentCommandDomainService commentCommandDomainService;
    private final InsightAssembler insightAssembler;
    private final CommentAssembler commentAssembler;
    private final NotificationCommandDomainService notificationCommandDomainService;
    private final MQPublishService mqPublishService;

    @Transactional
    public CommentCreateResponse create(CommentCreateRequest request) {
        Comment comment = commentCommandDomainService.create(insightAssembler.toCommentCreateDto(request));
        afterLeaveComment(comment);
        return commentAssembler.toCommentCreateResponse(comment);
    }

    @Transactional
    public CommentDeleteResponse delete(Long commentId) {
        return insightAssembler.toCommentDeleteResponse(
            commentCommandDomainService.delete(insightAssembler.toCommentDeleteDto(commentId))
        );
    }

    public void afterLeaveComment(Comment comment) {
        try {
            Long insightWriterId = comment.getInsight().getWriter().getId();
            Long commentWriterId = comment.getWriter().getId();
            if (!ObjectUtils.nullSafeEquals(insightWriterId, commentWriterId)) {
                String referenceId = String.valueOf(comment.getId());
                User insightWriter = comment.getInsight().getWriter();
                if (comment.getParent() != null) {
                    NotificationContents contents = NotificationContents.답글;
                    Notification notificationOfInsightWriter = Notification.of(insightWriter, contents, referenceId);
                    notificationCommandDomainService.save(notificationOfInsightWriter);
                    // note. 댓글 작성자 != 답글 작성자인 경우 알림 생성
                    if (!ObjectUtils.nullSafeEquals(comment.getParent().getWriter().getId(), commentWriterId)) {
                        Notification notificationOfCommentWriter = Notification.of(comment.getParent().getWriter(), contents, referenceId);
                        notificationCommandDomainService.save(notificationOfCommentWriter);
                    }
                    return;
                }
                Notification notification = Notification.of(insightWriter, NotificationContents.댓글, referenceId);
                notificationCommandDomainService.save(notification);
            }
        } catch (Throwable t) {
            log.warn("[CommentApiService::afterLeaveComment] 댓글 작성 완료 후 작업 실패 - commentId({}), insightId({})", comment.getId(), comment.getInsight().getId(), t);
        }
    }
}
