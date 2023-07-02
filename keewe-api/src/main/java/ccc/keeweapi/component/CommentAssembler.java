package ccc.keeweapi.component;

import ccc.keeweapi.dto.insight.response.ActiveUserCommentResponse;
import ccc.keeweapi.dto.insight.response.CommentResponse;
import ccc.keeweapi.dto.insight.response.CommentWriterResponse;
import ccc.keeweapi.dto.insight.response.DeletedUserCommentResponse;
import ccc.keeweapi.dto.insight.response.DeletedUserReplyResponse;
import ccc.keeweapi.dto.insight.response.InsightCommentCountResponse;
import ccc.keeweapi.dto.insight.response.PreviewCommentResponse;
import ccc.keeweapi.dto.insight.response.ActiveUserReplyResponse;
import ccc.keeweapi.dto.insight.response.ReplyResponse;
import ccc.keewedomain.persistence.domain.insight.Comment;
import ccc.keewedomain.persistence.domain.user.User;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class CommentAssembler {

    public PreviewCommentResponse toPreviewCommentResponse(Comment comment) {
        return PreviewCommentResponse.of(
                comment.getId(),
                toCommentWriterResponse(comment.getWriter()),
                comment.getContent(),
                comment.getCreatedAt().toString()
        );
    }

    public ActiveUserCommentResponse toCommentResponse(Comment comment, List<Comment> replies, Long replyNumber) {
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
        if (comment.getWriter().isDeleted()) {
            return toDeletedUserCommentResponse(comment, reply, replyNumber);
        }
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
        if (reply.getWriter().isDeleted()) {
            return DeletedUserReplyResponse.of(
                    reply.getId(),
                    reply.getParent().getId(),
                    "답글의 작성자를 찾을 수 없어요.",
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

    public DeletedUserCommentResponse toDeletedUserCommentResponse(Comment comment, Comment reply, Long replyNumber) {
        List<Comment> replies = Objects.nonNull(reply) ? List.of(reply) : List.of();
        return DeletedUserCommentResponse.of(
                comment.getId(),
                "댓글 작성자가 존재하지 않아요.",
                comment.getCreatedAt().toString(),
                replies.stream().map(this::toReplyResponse).collect(Collectors.toList()),
                replyNumber
        );
    }
}
