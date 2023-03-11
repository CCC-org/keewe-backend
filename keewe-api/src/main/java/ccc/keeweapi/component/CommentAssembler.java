package ccc.keeweapi.component;

import ccc.keeweapi.dto.insight.*;
import ccc.keewedomain.persistence.domain.insight.Comment;
import ccc.keewedomain.persistence.domain.user.User;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class CommentAssembler {

    public RepresentativeCommentResponse toRepresentativeCommentResponse(Comment comment) {
        return RepresentativeCommentResponse.of(
                comment.getId(),
                toCommentWriterResponse(comment.getWriter()),
                comment.getContent(),
                comment.getCreatedAt().toString()
        );
    }

    public CommentResponse toCommentResponse(Comment comment, List<Comment> replies, Long replyNumber) {

        return CommentResponse.of(
                comment.getId(),
                toCommentWriterResponse(comment.getWriter()),
                comment.getContent(),
                comment.getCreatedAt().toString(),
                replies.stream().map(this::toReplyResponse).collect(Collectors.toList()),
                replyNumber
        );
    }

    public CommentResponse toCommentResponse(Comment comment, Comment reply, Long replyNumber) {
        return toCommentResponse(comment, Objects.nonNull(reply) ? List.of(reply) : List.of(), replyNumber);
    }

    public CommentWriterResponse toCommentWriterResponse(User writer) {
        return CommentWriterResponse.of(
                writer.getId(),
                writer.getNickname(),
                writer.getRepTitleName(),
                writer.getProfilePhotoURL());
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

    public InsightCommentCountResponse toInsightCommentCountResponse(Long commentCount) {
        return InsightCommentCountResponse.of(commentCount);
    }
}
