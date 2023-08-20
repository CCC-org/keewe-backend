package ccc.keeweapi.component;

import ccc.keeweapi.dto.insight.response.ActiveCommentResponse;
import ccc.keeweapi.dto.insight.response.CommentCreateResponse;
import ccc.keeweapi.dto.insight.response.CommentResponse;
import ccc.keeweapi.dto.insight.response.CommentWriterResponse;
import ccc.keeweapi.dto.insight.response.PreviewCommentResponse;
import ccc.keeweapi.dto.insight.response.DeletedCommentResponse;
import ccc.keeweapi.dto.insight.response.InsightCommentCountResponse;
import ccc.keeweapi.dto.insight.response.ActivePreviewCommentResponse;
import ccc.keeweapi.dto.insight.response.ActiveReplyResponse;
import ccc.keeweapi.dto.insight.response.ReplyResponse;
import ccc.keeweapi.dto.insight.response.DeletedPreviewCommentResponse;
import ccc.keeweapi.dto.insight.response.DeletedReplyResponse;
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
            return toDeletedPreviewCommentResponse(comment);
        }
        return toActivePreviewCommentResponse(comment);
    }

    public ActivePreviewCommentResponse toActivePreviewCommentResponse(Comment comment) {
        return ActivePreviewCommentResponse.of(
                comment.getId(),
                toCommentWriterResponse(comment.getWriter()),
                comment.getContent(),
                comment.getCreatedAt().toString()
        );
    }

    public DeletedPreviewCommentResponse toDeletedPreviewCommentResponse(Comment comment) {
        return DeletedPreviewCommentResponse.of(
                comment.getId(),
                comment.getContent(),
                comment.getCreatedAt().toString()
        );
    }

    public ActiveCommentResponse toActiveCommentResponse(Comment comment, List<Comment> replies, Long replyNumber) {
        return ActiveCommentResponse.of(
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
            return toDeletedCommentResponse(comment, reply, replyNumber);
        }
        return toActiveCommentResponse(comment, Objects.nonNull(reply) ? List.of(reply) : List.of(), replyNumber);
    }

    public CommentWriterResponse toCommentWriterResponse(User writer) {
        return CommentWriterResponse.of(
                writer.getId(),
                writer.getNickname(),
                writer.getRepTitleName(),
                writer.getProfilePhotoURL());
    }

    public ReplyResponse toReplyResponse(Comment reply) {
        if (reply.isDeleted()) {
            return DeletedReplyResponse.of(
                    reply.getId(),
                    reply.getParent().getId(),
                    reply.getContent(),
                    reply.getCreatedAt().toString()
            );
        }

        return ActiveReplyResponse.of(
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

    public DeletedCommentResponse toDeletedCommentResponse(Comment comment, Comment reply, Long replyNumber) {
        List<Comment> replies = Objects.nonNull(reply) ? List.of(reply) : List.of();
        return DeletedCommentResponse.of(
                comment.getId(),
                comment.getContent(),
                comment.getCreatedAt().toString(),
                replies.stream().map(this::toReplyResponse).collect(Collectors.toList()),
                replyNumber
        );
    }

    public CommentCreateResponse toCommentCreateResponse(Comment comment) {
        return CommentCreateResponse.of(
                comment.getId(),
                comment.getContent(),
                comment.getCreatedAt().toString(),
                toCommentWriterResponse(comment.getWriter()),
                List.of(), // note. 답글 목록 및 개수 0개로 설정
                0L
        );
    }
}
