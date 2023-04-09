package ccc.keeweapi.dto.insight;

import ccc.keeweapi.dto.BlockFilteringResponse;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;


@Getter
@AllArgsConstructor(staticName = "of")
public class CommentResponse implements BlockFilteringResponse {

    private Long id;
    private CommentWriterResponse writer;
    private String content;
    private String createdAt;
    private List<ReplyResponse> replies;
    private Long totalReply;

    @Override
    @JsonIgnore
    public Long getUserId() {
        return writer.getId();
    }
}
