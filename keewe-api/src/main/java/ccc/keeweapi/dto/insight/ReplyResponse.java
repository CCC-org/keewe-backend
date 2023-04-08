package ccc.keeweapi.dto.insight;

import ccc.keeweapi.dto.BlockFilteringResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(staticName = "of")
public class ReplyResponse implements BlockFilteringResponse {

    private CommentWriterResponse writer;
    private Long id;
    private Long parentId;
    private String content;
    private String createdAt;

    @Override
    public Long userId() {
        return writer.getId();
    }
}
