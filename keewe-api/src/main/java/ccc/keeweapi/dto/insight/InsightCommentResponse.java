package ccc.keeweapi.dto.insight;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor(staticName = "of")
public class InsightCommentResponse {

    private Long total;
    private List<CommentResponse> comments;
}
