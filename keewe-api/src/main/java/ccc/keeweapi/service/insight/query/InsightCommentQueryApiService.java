package ccc.keeweapi.service.insight.query;

import ccc.keeweapi.component.CommentAssembler;
import ccc.keeweapi.dto.insight.CommentResponse;
import ccc.keeweapi.dto.insight.InsightCommentCountResponse;
import ccc.keeweapi.dto.insight.ReplyResponse;
import ccc.keeweapi.dto.insight.RepresentativeCommentResponse;
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

    // 인사이트의 대표 댓글
    // 답글의 수가 가장 많은 댓글 1개 + 답글 최대 2개
    // 모든 댓글에 답글이 없는 경우 작성순 댓글 3개
    @Transactional(readOnly = true)
    public List<RepresentativeCommentResponse> getRepresentativeComments(Long insightId) {
        List<Comment> comments = commentDomainService.getComments(insightId, CursorPageable.of(Long.MAX_VALUE, 3L));
        commentDomainService.findLatestCommentByWriter(SecurityUtil.getUser())
                .ifPresent(myLatestComment -> {
                    comments.removeIf(comment -> comment.getId().equals(myLatestComment.getId()));
                    comments.add(0, myLatestComment);
                });

        return comments.stream()
                .map(commentAssembler::toRepresentativeCommentResponse)
                .limit(3)
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
