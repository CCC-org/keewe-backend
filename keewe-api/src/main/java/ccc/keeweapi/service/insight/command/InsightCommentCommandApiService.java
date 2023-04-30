package ccc.keeweapi.service.insight.command;

import ccc.keeweapi.component.InsightAssembler;
import ccc.keeweapi.dto.insight.request.CommentCreateRequest;
import ccc.keeweapi.dto.insight.response.CommentCreateResponse;
import ccc.keeweapi.dto.insight.response.CommentDeleteResponse;
import ccc.keewedomain.persistence.domain.insight.Comment;
import ccc.keewedomain.persistence.domain.notification.Notification;
import ccc.keewedomain.persistence.domain.notification.enums.NotificationContents;
import ccc.keewedomain.service.insight.CommentDomainService;
import ccc.keewedomain.service.notification.command.NotificationCommandDomainService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class InsightCommentCommandApiService {

    private final CommentDomainService commentDomainService;
    private final InsightAssembler insightAssembler;
    private final NotificationCommandDomainService notificationCommandDomainService;

    @Transactional
    public CommentCreateResponse create(CommentCreateRequest request) {
        Comment comment = commentDomainService.create(insightAssembler.toCommentCreateDto(request));
        afterLeaveComment(comment);
        return insightAssembler.toCommentCreateResponse(comment);
    }

    @Transactional
    public CommentDeleteResponse delete(Long commentId) {
        return insightAssembler.toCommentDeleteResponse(
                commentDomainService.delete(insightAssembler.toCommentDeleteDto(commentId)));
    }

    public void afterLeaveComment(Comment comment) {
        try {
            NotificationContents contents = (comment.getParent() != null) ? NotificationContents.답글 : NotificationContents.댓글;
            Notification notification = Notification.of(comment.getInsight().getWriter(), contents, String.valueOf(comment.getId()));
            notificationCommandDomainService.save(notification);
        } catch (Throwable t) {
            log.warn("[CommentApiService::afterLeaveComment] 댓글 작성 완료 후 작업 실패 - commentId({}), insightId({})", comment.getId(), comment.getInsight().getId(), t);
        }
    }
}
