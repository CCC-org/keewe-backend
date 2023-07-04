package ccc.keeweapi.dto.insight.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(staticName = "of")
public class DeletedReplyResponse implements ReplyResponse {
    private Long id;
    private Long parentId;
    private String content;
    private String createdAt;

    @Override
    @JsonIgnore
    public Long getUserId() {
        return Long.MIN_VALUE;
    }
}
