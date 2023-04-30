package ccc.keeweapi.service.insight.query;

import ccc.keeweapi.component.CommentAssembler;
import ccc.keeweapi.dto.insight.CommentResponse;
import ccc.keeweapi.dto.insight.InsightCommentCountResponse;
import ccc.keeweapi.dto.insight.PreviewCommentResponse;
import ccc.keeweapi.dto.insight.ReplyResponse;
import ccc.keeweapi.utils.BlockFilterUtil;
import ccc.keeweapi.utils.SecurityUtil;
import ccc.keewedomain.persistence.domain.insight.Comment;
import ccc.keewedomain.persistence.repository.utils.CursorPageable;
import ccc.keewedomain.service.insight.CommentDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InsightCommentQueryApiService {

    private final CommentDomainService commentDomainService;
    private final CommentAssembler commentAssembler;
    private final BlockFilterUtil blockFilterUtil;

    @Transactional(readOnly = true)
    public List<PreviewCommentResponse> getPreviewComments(Long insightId) {
        blockFilterUtil.filterInsightWriter(insightId);
        List<Comment> comments = commentDomainService.getComments(insightId, CursorPageable.of(Long.MAX_VALUE, 3L));
        commentDomainService.findLatestCommentByWriter(SecurityUtil.getUser(), insightId)
                .ifPresent(myLatestComment -> {
                    comments.removeIf(comment -> comment.getId().equals(myLatestComment.getId()));
                    comments.add(0, myLatestComment);
                });

        List<PreviewCommentResponse> responses = comments.stream()
                .limit(3)
                .map(commentAssembler::toPreviewCommentResponse)
                .collect(Collectors.toList());
        return blockFilterUtil.filterUserInResponse(responses);
    }

    @Transactional(readOnly = true)
    public List<CommentResponse> getCommentsWithFirstReply(Long insightId, CursorPageable<Long> cPage) {
        blockFilterUtil.filterInsightWriter(insightId);
        List<Comment> comments = commentDomainService.getComments(insightId, cPage);
        Map<Long, Comment> firstReplyPerParentId = commentDomainService.getFirstReplies(comments);
        Map<Long, Long> replyNumberPerParentId = commentDomainService.getReplyNumbers(comments);

        List<CommentResponse> responses = comments.stream()
                .map(comment -> commentAssembler.toCommentResponse(
                        comment,
                        firstReplyPerParentId.get(comment.getId()),
                        replyNumberPerParentId.getOrDefault(comment.getId(), 0L)
                ))
                .collect(Collectors.toList());
        return blockFilterUtil.filterUserInResponse(responses);
    }

    @Transactional(readOnly = true)
    public List<ReplyResponse> getReplies(Long parentId, CursorPageable<Long> cPage) {
        List<ReplyResponse> responses = commentDomainService.getReplies(parentId, cPage).stream()
                .map(commentAssembler::toReplyResponse)
                .collect(Collectors.toList());
        return blockFilterUtil.filterUserInResponse(responses);
    }

    public InsightCommentCountResponse getCommentCount(Long insightId) {
        blockFilterUtil.filterInsightWriter(insightId);
        Long userId = SecurityUtil.getUserId();
        return commentAssembler.toInsightCommentCountResponse(commentDomainService.countByInsightId(insightId, userId));
    }
}
