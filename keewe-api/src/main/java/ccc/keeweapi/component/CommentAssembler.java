package ccc.keeweapi.component;

import ccc.keeweapi.dto.insight.response.ActiveUserCommentResponse;
import ccc.keeweapi.dto.insight.response.CommentResponse;
import ccc.keeweapi.dto.insight.response.CommentWriterResponse;
import ccc.keeweapi.dto.insight.response.PreviewCommentResponse;
import ccc.keeweapi.dto.insight.response.WithdrawnUserCommentResponse;
import ccc.keeweapi.dto.insight.response.DeletedUserReplyResponse;
import ccc.keeweapi.dto.insight.response.InsightCommentCountResponse;
import ccc.keeweapi.dto.insight.response.ActiveUserPreviewCommentResponse;
import ccc.keeweapi.dto.insight.response.ActiveUserReplyResponse;
import ccc.keeweapi.dto.insight.response.ReplyResponse;
import ccc.keeweapi.dto.insight.response.WithdrawnUserPreviewCommentResponse;
import ccc.keewedomain.persistence.domain.insight.Comment;
import ccc.keewedomain.persistence.domain.user.User;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class CommentAssembler {

    public PreviewCommentResponse toPreviewCommentResponseInterface(Comment comment) {
        if (comment.isDeleted()) {
            return toWithdrawnUserPreviewCommentResponse(comment);
        }
        return toActiveUserPreviewCommentResponse(comment);
    }

    public ActiveUserPreviewCommentResponse toActiveUserPreviewCommentResponse(Comment comment) {
        return ActiveUserPreviewCommentResponse.of(
                comment.getId(),
                toCommentWriterResponse(comment.getWriter()),
                comment.getContent(),
                comment.getCreatedAt().toString()
        );
    }

    public WithdrawnUserPreviewCommentResponse toWithdrawnUserPreviewCommentResponse(Comment comment) {
        return WithdrawnUserPreviewCommentResponse.of(
                comment.getId(),
                comment.getContent(),
                comment.getCreatedAt().toString()
        );
    }

    public ActiveUserCommentResponse toActiveUserCommentResponse(Comment comment, List<Comment> replies, Long replyNumber) {
        return ActiveUserCommentResponse.of(
                comment.getId(),
                toCommentWriterResponse(comment.getWriter()),
                comment.getContent(),
                comment.getCreatedAt().toString(),
                replies.stream().map(this::toReplyResponse).collect(Collectors.toList()),
                replyNumber
        );
    }

    public CommentResponse toCommentResponse(Comment comment, Comment reply, Long replyNumber) {
        if (comment.isDeleted()) {
            return toWithdrawnUserCommentResponse(comment, reply, replyNumber);
        }
        return toActiveUserCommentResponse(comment, Objects.nonNull(reply) ? List.of(reply) : List.of(), replyNumber);
    }

    public CommentWriterResponse toCommentWriterResponse(User writer) {
        return CommentWriterResponse.of(
                writer.getId(),
                writer.getNickname(),
                writer.getRepTitleName(),
                writer.getProfilePhotoURL());
    }

    public ReplyResponse toReplyResponse(Comment reply) {
        if (reply.getWriter().isDeleted()) {
            return DeletedUserReplyResponse.of(
                    reply.getId(),
                    reply.getParent().getId(),
                    reply.getContent(),
                    reply.getCreatedAt().toString()
            );
        }

        return ActiveUserReplyResponse.of(
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

    public WithdrawnUserCommentResponse toWithdrawnUserCommentResponse(Comment comment, Comment reply, Long replyNumber) {
        List<Comment> replies = Objects.nonNull(reply) ? List.of(reply) : List.of();
        return WithdrawnUserCommentResponse.of(
                comment.getId(),
                comment.getContent(),
                comment.getCreatedAt().toString(),
                replies.stream().map(this::toReplyResponse).collect(Collectors.toList()),
                replyNumber
        );
    }
}
