package ccc.keeweapi.dto.insight;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(staticName = "of")
public class PreviewCommentResponse {
    private Long id;
    private CommentWriterResponse writer;
    private String content;
    private String createdAt;
}
