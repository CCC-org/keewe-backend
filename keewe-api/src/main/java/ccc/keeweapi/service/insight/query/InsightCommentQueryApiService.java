package ccc.keeweapi.service.insight.query;

import static ccc.keewecore.consts.KeeweConsts.REPRESENTATIVE_COMMENT_REPLY_LIMIT;

import ccc.keeweapi.component.CommentAssembler;
import ccc.keeweapi.dto.insight.CommentResponse;
import ccc.keeweapi.dto.insight.ReplyResponse;
import ccc.keeweapi.dto.insight.RepresentativeCommentResponse;
import ccc.keewedomain.persistence.domain.insight.Comment;
import ccc.keewedomain.persistence.repository.utils.CursorPageable;
import ccc.keewedomain.service.insight.CommentDomainService;
import java.util.HashMap;
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
    public RepresentativeCommentResponse getRepresentativeComments(Long insightId) {
        List<Comment> comments = commentDomainService.getRepresentativeCommentsWithWriter(insightId);
        Map<Long, Long> replyNumberPerParentId = commentDomainService.getReplyNumbers(comments);
        Long total = commentDomainService.getCommentNumberByInsightId(insightId);
        Map<Long, List<Comment>> replyPerParentId = new HashMap<>();

        if (comments.size() == 1) {
            replyPerParentId = commentDomainService.getReplies(
                            comments.get(0).getId(),
                            CursorPageable.of(Long.MAX_VALUE, REPRESENTATIVE_COMMENT_REPLY_LIMIT)
                    ).stream()
                    .collect(Collectors.groupingBy(reply -> reply.getParent().getId()));
        }

        return commentAssembler.toRepresentativeCommentResponse(comments, replyPerParentId, replyNumberPerParentId, total);
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
}
