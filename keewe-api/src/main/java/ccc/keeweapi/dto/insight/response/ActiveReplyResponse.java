package ccc.keeweapi.dto.insight.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(staticName = "of")
public class ActiveReplyResponse implements ReplyResponse {

    private CommentWriterResponse writer;
    private Long id;
    private Long parentId;
    private String content;
    private String createdAt;

    @Override
    @JsonIgnore
    public Long getUserId() {
        return writer.getId();
    }
}
