package ccc.keeweapi.dto.insight.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(staticName = "of")
public class ActivePreviewCommentResponse implements PreviewCommentResponse {
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
