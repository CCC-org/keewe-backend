package ccc.keeweapi.dto.insight.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor(staticName = "of")
@Getter
public class CommentCreateResponse {
    private Long id;
    private String content;
    private String createdAt;
    private CommentWriterResponse writer;
    private List<ReplyResponse> replies;
    private Long totalReply;
}
