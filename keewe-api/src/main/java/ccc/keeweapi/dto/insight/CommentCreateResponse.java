package ccc.keeweapi.dto.insight;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class CommentCreateResponse {

    private Long commentId;

    public static CommentCreateResponse of(Long commentId) {
        return new CommentCreateResponse(commentId);
    }
}
