package ccc.keeweapi.dto.insight.response;

import ccc.keeweapi.dto.BlockFilteringResponse;

public interface ReplyResponse extends BlockFilteringResponse {
    Long getId();
    Long getParentId();
    String getContent();
    String getCreatedAt();
}
