package ccc.keeweapi.dto.insight.response;

import ccc.keeweapi.dto.BlockFilteringResponse;

import java.util.List;

public interface CommentResponse extends BlockFilteringResponse {
    Long getId();
    String getContent();
    String getCreatedAt();
    List<ReplyResponse> getReplies();
    Long getTotalReply();
}
