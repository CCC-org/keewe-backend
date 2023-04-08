package ccc.keeweapi.dto.insight;

import ccc.keeweapi.dto.BlockFilteringResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(staticName = "of")
public class PreviewCommentResponse implements BlockFilteringResponse {
    private Long id;
    private CommentWriterResponse writer;
    private String content;
    private String createdAt;

    @Override
    public Long userId() {
        return writer.getId();
    }
}
