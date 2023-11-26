package ccc.keeweapi.dto.insight.response;

import ccc.keeweapi.dto.BlockFilteringResponse;

import java.util.List;

public interface CommentResponse extends BlockFilteringResponse {
    Long getId();
    String getContent();
    String getCreatedAt();
    // note. 첫 댓글 1개 응답 (최신 버전 미사용, Native 하위버전 호환을 위해 No Remove)
    List<ReplyResponse> getReplies();
    Long getTotalReply();
}
