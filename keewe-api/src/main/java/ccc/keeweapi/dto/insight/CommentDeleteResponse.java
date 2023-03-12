package ccc.keeweapi.dto.insight;

import lombok.Getter;

@Getter
public class CommentDeleteResponse {

    private Long commentId;

    public static CommentDeleteResponse of(Long commentId) {
        CommentDeleteResponse dto = new CommentDeleteResponse();
        dto.commentId = commentId;

        return dto;
    }
}
