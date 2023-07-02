package ccc.keeweapi.dto.insight.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(staticName = "of")
public class WithdrawnUserPreviewCommentResponse implements PreviewCommentResponse {
    private Long id;
    private String content;
    private String createdAt;

    @Override
    @JsonIgnore
    public Long getUserId() {
        return Long.MIN_VALUE;
    }
}
