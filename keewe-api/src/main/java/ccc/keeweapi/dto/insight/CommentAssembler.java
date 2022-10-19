package ccc.keeweapi.dto.insight;

import ccc.keewedomain.persistence.domain.insight.Comment;
import ccc.keewedomain.persistence.domain.user.User;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class CommentAssembler {

    public InsightCommentResponse toInsightCommentResponse(List<Comment> comments, Map<Long, List<Comment>> replyPerParentId, Long total) {
        List<CommentResponse> commentResponses = comments.stream()
                .map(comment -> toCommentResponse(comment, replyPerParentId.getOrDefault(comment.getId(), List.of())))
                .collect(Collectors.toList());

        return InsightCommentResponse.of(total, commentResponses);
    }

    public CommentResponse toCommentResponse(Comment comment, List<Comment> replies) {

        return CommentResponse.of(
                comment.getId(),
                toCommentWriterResponse(comment.getWriter()),
                comment.getContent(),
                comment.getCreatedAt().toString(),
                replies.stream().map(this::toReplyResponse).collect(Collectors.toList())
        );
    }

    public CommentWriterResponse toCommentWriterResponse(User writer) {
        //TODO 타이틀, 사진 정해지면 변경
        return CommentWriterResponse.of(writer.getId(), writer.getNickname(), "title", "www.api-keewe.com/images");
    }

    public ReplyResponse toReplyResponse(Comment reply) {
        return ReplyResponse.of(
                toCommentWriterResponse(reply.getWriter()),
                reply.getId(),
                reply.getParent().getId(),
                reply.getContent(),
                reply.getCreatedAt().toString()
        );
    }

}
