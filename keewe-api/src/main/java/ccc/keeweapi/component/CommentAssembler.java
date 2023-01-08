package ccc.keeweapi.component;

import ccc.keeweapi.dto.insight.CommentResponse;
import ccc.keeweapi.dto.insight.CommentWriterResponse;
import ccc.keeweapi.dto.insight.ReplyResponse;
import ccc.keeweapi.dto.insight.RepresentativeCommentResponse;
import ccc.keewedomain.persistence.domain.insight.Comment;
import ccc.keewedomain.persistence.domain.user.User;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class CommentAssembler {

    public RepresentativeCommentResponse toRepresentativeCommentResponse(
            List<Comment> comments,
            Map<Long, List<Comment>> replyPerParentId,
            Map<Long, Long> replyNumberPerParentId,
            Long total) {

        List<CommentResponse> commentResponses = comments.stream()
                .map(comment -> toCommentResponse(
                        comment,
                        replyPerParentId.getOrDefault(comment.getId(), List.of()),
                        replyNumberPerParentId.getOrDefault(comment.getId(), 0L)
                ))
                .collect(Collectors.toList());

        return RepresentativeCommentResponse.of(total, commentResponses);
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

}
