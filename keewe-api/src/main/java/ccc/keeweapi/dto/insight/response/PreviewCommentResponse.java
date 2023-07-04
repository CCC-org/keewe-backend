package ccc.keeweapi.dto.insight.response;

import ccc.keeweapi.dto.BlockFilteringResponse;

public interface PreviewCommentResponse extends BlockFilteringResponse {
    Long getId();
    String getContent();
    String getCreatedAt();
}
