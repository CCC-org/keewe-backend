package ccc.keeweapi.dto.insight.response;

import ccc.keeweapi.dto.BlockFilteringResponse;
import com.fasterxml.jackson.annotation.JsonIgnore;
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
    @JsonIgnore
    public Long getUserId() {
        return writer.getId();
    }
}
