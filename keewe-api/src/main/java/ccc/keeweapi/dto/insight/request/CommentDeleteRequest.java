package ccc.keeweapi.dto.insight.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(staticName = "of")
public class CommentDeleteRequest {

    private Long userId;
    private Long commentId;
}
