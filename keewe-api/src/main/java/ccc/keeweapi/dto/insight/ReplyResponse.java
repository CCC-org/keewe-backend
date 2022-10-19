package ccc.keeweapi.dto.insight;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(staticName = "of")
public class ReplyResponse {

    private CommentWriterResponse writer;
    private Long id;
    private Long parentId;
    private String content;
    private String createdAt;
}
