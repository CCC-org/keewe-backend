package ccc.keeweapi.dto.insight.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor(staticName = "of")
public class WithdrawnUserCommentResponse implements CommentResponse {
    private Long id;
    private String content;
    private String createdAt;
    private List<ReplyResponse> replies;
    private Long totalReply;

    @Override
    @JsonIgnore
    public Long getUserId() {
        return Long.MIN_VALUE;
    }
}
