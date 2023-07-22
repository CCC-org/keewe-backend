package ccc.keeweapi.service.insight.query;

import ccc.keeweapi.component.CommentAssembler;
import ccc.keeweapi.dto.insight.response.CommentResponse;
import ccc.keeweapi.dto.insight.response.InsightCommentCountResponse;
import ccc.keeweapi.dto.insight.response.PreviewCommentResponse;
import ccc.keeweapi.dto.insight.response.ReplyResponse;
import ccc.keeweapi.utils.BlockedResourceManager;
import ccc.keeweapi.utils.SecurityUtil;
import ccc.keewedomain.persistence.domain.insight.Comment;
import ccc.keewedomain.persistence.domain.user.User;
import ccc.keewedomain.persistence.repository.utils.CursorPageable;
import ccc.keewedomain.service.insight.query.CommentQueryDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InsightCommentQueryApiService {

    private final CommentQueryDomainService commentQueryDomainService;
    private final CommentAssembler commentAssembler;
    private final BlockedResourceManager blockedResourceManager;

    @Transactional(readOnly = true)
    public List<PreviewCommentResponse> getPreviewComments(Long insightId) {
        blockedResourceManager.validateAccessibleInsight(insightId);
        User user = SecurityUtil.getUser();
        List<Comment> comments = commentQueryDomainService.getCommentsWithoutBlocked(insightId, CursorPageable.of(Long.MIN_VALUE, 3L), user.getId());
        commentQueryDomainService.findLatestCommentByWriter(user, insightId)
                .ifPresent(myLatestComment -> {
                    // note. 나의 최신 댓글이 기존의 리스트에서 존재하는 경우 제거. 나의 최신 댓글을 맨 앞에 추가.
                    comments.removeIf(comment -> comment.getId().equals(myLatestComment.getId()));
                    comments.add(0, myLatestComment);
                });

        List<PreviewCommentResponse> responses = comments.stream()
                .limit(3)
                .map(commentAssembler::toPreviewCommentResponseInterface)
                .collect(Collectors.toList());
        return blockedResourceManager.filterBlockedUsers(responses);
    }

    @Transactional(readOnly = true)
    public List<CommentResponse> getCommentsWithFirstReply(Long insightId, CursorPageable<Long> cPage) {
        Long userId = SecurityUtil.getUserId();
        blockedResourceManager.validateAccessibleInsight(insightId);
        List<Comment> comments = commentQueryDomainService.getComments(insightId, cPage);
        Map<Long, Comment> firstReplyPerParentId = commentQueryDomainService.getFirstReplies(comments, userId);
        Map<Long, Long> replyNumberPerParentId = commentQueryDomainService.getReplyNumbers(comments, userId);

        List<CommentResponse> responses = comments.stream()
                .filter(comment -> {
                    Long replyNumber = replyNumberPerParentId.getOrDefault(comment.getId(), 0L);
                    return !(replyNumber <= 0 && comment.isDeleted());
                })
                .map(comment -> commentAssembler.toCommentResponse(
                        comment,
                        firstReplyPerParentId.get(comment.getId()),
                        replyNumberPerParentId.getOrDefault(comment.getId(), 0L)
                ))
                .collect(Collectors.toList());
        return blockedResourceManager.filterBlockedUsers(responses);
    }

    @Transactional(readOnly = true)
    public List<ReplyResponse> getReplies(Long parentId, CursorPageable<Long> cPage) {
        List<ReplyResponse> responses = commentQueryDomainService.getReplies(parentId, cPage).stream()
                .map(commentAssembler::toReplyResponse)
                .collect(Collectors.toList());
        return blockedResourceManager.filterBlockedUsers(responses);
    }

    public InsightCommentCountResponse getCommentCount(Long insightId) {
        blockedResourceManager.validateAccessibleInsight(insightId);
        Long userId = SecurityUtil.getUserId();
        return commentAssembler.toInsightCommentCountResponse(commentQueryDomainService.countByInsightId(insightId, userId));
    }
}
