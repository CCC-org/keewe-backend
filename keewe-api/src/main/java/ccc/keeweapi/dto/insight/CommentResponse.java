package ccc.keeweapi.dto.insight;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;


@Getter
@AllArgsConstructor(staticName = "of")
public class CommentResponse {

    private Long id;
    private CommentWriterResponse writer;
    private String content;
    private String createdAt;
    private List<ReplyResponse> replies;
    private Long totalReply;
}
