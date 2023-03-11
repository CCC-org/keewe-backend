package ccc.keeweapi.service.insight.query;

import ccc.keeweapi.component.CommentAssembler;
import ccc.keeweapi.dto.insight.CommentResponse;
import ccc.keeweapi.dto.insight.InsightCommentCountResponse;
import ccc.keeweapi.dto.insight.ReplyResponse;
import ccc.keeweapi.dto.insight.PreviewCommentResponse;
import ccc.keeweapi.utils.SecurityUtil;
import ccc.keewedomain.persistence.domain.insight.Comment;
import ccc.keewedomain.persistence.repository.utils.CursorPageable;
import ccc.keewedomain.service.insight.CommentDomainService;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class InsightCommentQueryApiService {

    private final CommentDomainService commentDomainService;
    private final CommentAssembler commentAssembler;

    @Transactional(readOnly = true)
    public List<PreviewCommentResponse> getPreviewComments(Long insightId) {
        List<Comment> comments = commentDomainService.getComments(insightId, CursorPageable.of(Long.MAX_VALUE, 3L));
        commentDomainService.findLatestCommentByWriter(SecurityUtil.getUser())
                .ifPresent(myLatestComment -> {
                    comments.removeIf(comment -> comment.getId().equals(myLatestComment.getId()));
                    comments.add(0, myLatestComment);
                });

        return comments.stream()
                .limit(3)
                .map(commentAssembler::toPreviewCommentResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<CommentResponse> getCommentsWithFirstReply(Long insightId, CursorPageable<Long> cPage) {
        List<Comment> comments = commentDomainService.getComments(insightId, cPage);
        Map<Long, Comment> firstReplyPerParentId = commentDomainService.getFirstReplies(comments);
        Map<Long, Long> replyNumberPerParentId = commentDomainService.getReplyNumbers(comments);

        return comments.stream()
                .map(comment -> commentAssembler.toCommentResponse(
                        comment,
                        firstReplyPerParentId.get(comment.getId()),
                        replyNumberPerParentId.getOrDefault(comment.getId(), 0L)
                ))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ReplyResponse> getReplies(Long parentId, CursorPageable<Long> cPage) {
        return commentDomainService.getReplies(parentId, cPage).stream()
                .map(commentAssembler::toReplyResponse)
                .collect(Collectors.toList());
    }

    public InsightCommentCountResponse getCommentCount(Long insightId) {
        return commentAssembler.toInsightCommentCountResponse(commentDomainService.countByInsightId(insightId));
    }
}
